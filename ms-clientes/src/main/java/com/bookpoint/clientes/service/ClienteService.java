package com.bookpoint.clientes.service;

import com.bookpoint.clientes.dto.ClienteRequestDTO;
import com.bookpoint.clientes.dto.ClienteResponseDTO;
import com.bookpoint.clientes.dto.DireccionRequestDTO;
import com.bookpoint.clientes.dto.DireccionResponseDTO;
import com.bookpoint.clientes.exception.RecursoNoEncontradoException;
import com.bookpoint.clientes.model.Cliente;
import com.bookpoint.clientes.model.Direccion;
import com.bookpoint.clientes.repository.ClienteRepository;
import com.bookpoint.clientes.repository.DireccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Logica de negocio de los clientes y sus direcciones.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final DireccionRepository direccionRepository;

    // ------------------------- Clientes -------------------------

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public ClienteResponseDTO obtenerPorId(Long id) {
        return convertirAResponse(buscarClienteOFallar(id));
    }

    @Transactional
    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con el email: " + dto.getEmail());
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setActivo(true);

        Cliente guardado = clienteRepository.save(cliente);
        log.info("Cliente creado: {} {} (id={})",
                guardado.getNombre(), guardado.getApellido(), guardado.getId());
        return convertirAResponse(guardado);
    }

    @Transactional
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = buscarClienteOFallar(id);

        // Solo validamos duplicado si el email cambio
        boolean cambioEmail = !cliente.getEmail().equals(dto.getEmail());
        if (cambioEmail && clienteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con el email: " + dto.getEmail());
        }

        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());

        Cliente actualizado = clienteRepository.save(cliente);
        log.info("Cliente actualizado: id={}", actualizado.getId());
        return convertirAResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = buscarClienteOFallar(id);
        clienteRepository.delete(cliente); // borra tambien sus direcciones (cascade)
        log.info("Cliente eliminado: id={}", id);
    }

    // ------------------------- Direcciones (sub-recurso) -------------------------

    public List<DireccionResponseDTO> listarDirecciones(Long clienteId) {
        Cliente cliente = buscarClienteOFallar(clienteId);
        return cliente.getDirecciones().stream()
                .map(this::convertirDireccion)
                .toList();
    }

    @Transactional
    public DireccionResponseDTO agregarDireccion(Long clienteId, DireccionRequestDTO dto) {
        Cliente cliente = buscarClienteOFallar(clienteId);

        Direccion direccion = new Direccion();
        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setComuna(dto.getComuna());
        direccion.setCiudad(dto.getCiudad());
        direccion.setRegion(dto.getRegion());

        cliente.agregarDireccion(direccion); // enlaza ambos lados de la relacion
        clienteRepository.save(cliente);     // la direccion se guarda por cascade

        // Tomamos la ultima direccion agregada para devolverla con su id ya generado
        Direccion guardada = cliente.getDirecciones().get(cliente.getDirecciones().size() - 1);
        log.info("Direccion agregada al cliente id={} (direccion id={})", clienteId, guardada.getId());
        return convertirDireccion(guardada);
    }

    @Transactional
    public void eliminarDireccion(Long clienteId, Long direccionId) {
        Cliente cliente = buscarClienteOFallar(clienteId);

        Direccion direccion = cliente.getDirecciones().stream()
                .filter(d -> d.getId().equals(direccionId))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "La direccion " + direccionId + " no pertenece al cliente " + clienteId));

        cliente.quitarDireccion(direccion);
        clienteRepository.save(cliente); // orphanRemoval borra la direccion de la BD
        log.info("Direccion id={} eliminada del cliente id={}", direccionId, clienteId);
    }

    // ------------------------- Apoyo -------------------------

    private Cliente buscarClienteOFallar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el cliente con id " + id));
    }

    private ClienteResponseDTO convertirAResponse(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        dto.setActivo(cliente.getActivo());
        dto.setDirecciones(cliente.getDirecciones().stream()
                .map(this::convertirDireccion)
                .toList());
        return dto;
    }

    private DireccionResponseDTO convertirDireccion(Direccion direccion) {
        DireccionResponseDTO dto = new DireccionResponseDTO();
        dto.setId(direccion.getId());
        dto.setCalle(direccion.getCalle());
        dto.setNumero(direccion.getNumero());
        dto.setComuna(direccion.getComuna());
        dto.setCiudad(direccion.getCiudad());
        dto.setRegion(direccion.getRegion());
        return dto;
    }
}
