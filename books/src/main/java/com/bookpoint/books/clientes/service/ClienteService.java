package com.bookpoint.books.clientes.service;

import com.bookpoint.books.clientes.dto.ClienteDTO;
import com.bookpoint.books.clientes.dto.DireccionDTO;
import com.bookpoint.books.clientes.model.Cliente;
import com.bookpoint.books.clientes.model.Direccion;
import com.bookpoint.books.clientes.repository.ClienteRepository;
import com.bookpoint.books.exception.RecursoNoEncontradoException;
import com.bookpoint.books.exception.ReglaNegocioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Logica de clientes web. Maneja la creacion del cliente con sus
 * direcciones asociadas en una sola operacion gracias al cascade.
 */
@Service
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> listar() {
        log.info("Listando clientes");
        return repository.findAll().stream().map(this::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public ClienteDTO buscarPorId(Long id) {
        log.info("Buscando cliente id {}", id);
        return aDTO(repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Cliente con id " + id + " no encontrado")));
    }

    @Transactional
    public ClienteDTO crear(ClienteDTO dto) {
        log.info("Creando cliente: {}", dto.getEmail());
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ReglaNegocioException("Ya existe un cliente con el email " + dto.getEmail());
        }
        Cliente c = aEntidad(dto);
        if (c.getActivo() == null) c.setActivo(true);
        // Mantener la asociacion bidireccional: cada direccion conoce a su cliente.
        c.getDirecciones().forEach(d -> d.setCliente(c));
        return aDTO(repository.save(c));
    }

    @Transactional
    public ClienteDTO actualizar(Long id, ClienteDTO dto) {
        log.info("Actualizando cliente id {}", id);
        Cliente c = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Cliente con id " + id + " no encontrado"));
        c.setNombre(dto.getNombre());
        c.setApellido(dto.getApellido());
        c.setEmail(dto.getEmail());
        c.setPassword(dto.getPassword());
        c.setTelefono(dto.getTelefono());
        if (dto.getActivo() != null) c.setActivo(dto.getActivo());

        c.getDirecciones().clear();
        if (dto.getDirecciones() != null) {
            for (DireccionDTO d : dto.getDirecciones()) {
                c.getDirecciones().add(Direccion.builder()
                        .calle(d.getCalle()).ciudad(d.getCiudad())
                        .region(d.getRegion()).codigoPostal(d.getCodigoPostal())
                        .cliente(c).build());
            }
        }
        return aDTO(repository.save(c));
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando cliente id {}", id);
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Cliente con id " + id + " no encontrado");
        }
        repository.deleteById(id);
    }

    private ClienteDTO aDTO(Cliente c) {
        List<DireccionDTO> ds = new ArrayList<>();
        if (c.getDirecciones() != null) {
            for (Direccion d : c.getDirecciones()) {
                ds.add(DireccionDTO.builder().id(d.getId())
                        .calle(d.getCalle()).ciudad(d.getCiudad())
                        .region(d.getRegion()).codigoPostal(d.getCodigoPostal()).build());
            }
        }
        return ClienteDTO.builder().id(c.getId()).nombre(c.getNombre()).apellido(c.getApellido())
                .email(c.getEmail()).password(c.getPassword()).telefono(c.getTelefono())
                .activo(c.getActivo()).direcciones(ds).build();
    }

    private Cliente aEntidad(ClienteDTO d) {
        Cliente c = Cliente.builder()
                .id(d.getId()).nombre(d.getNombre()).apellido(d.getApellido())
                .email(d.getEmail()).password(d.getPassword()).telefono(d.getTelefono())
                .activo(d.getActivo()).direcciones(new ArrayList<>()).build();
        if (d.getDirecciones() != null) {
            for (DireccionDTO dd : d.getDirecciones()) {
                c.getDirecciones().add(Direccion.builder()
                        .calle(dd.getCalle()).ciudad(dd.getCiudad())
                        .region(dd.getRegion()).codigoPostal(dd.getCodigoPostal()).build());
            }
        }
        return c;
    }
}
