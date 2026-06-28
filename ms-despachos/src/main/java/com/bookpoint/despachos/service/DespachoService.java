package com.bookpoint.despachos.service;

import com.bookpoint.despachos.client.PedidoClient;
import com.bookpoint.despachos.dto.DespachoRequestDTO;
import com.bookpoint.despachos.dto.DespachoResponseDTO;
import com.bookpoint.despachos.exception.RecursoNoEncontradoException;
import com.bookpoint.despachos.model.Despacho;
import com.bookpoint.despachos.model.EstadoDespacho;
import com.bookpoint.despachos.repository.DespachoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Logica de negocio de los despachos.
 *
 * Al crear un despacho, valida el pedido en ms-pedidos e impide crear dos
 * despachos para el mismo pedido. Al marcar ENTREGADO, registra la fecha de entrega.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DespachoService {

    private final DespachoRepository despachoRepository;
    private final PedidoClient pedidoClient;

    // ------------------------- Lecturas -------------------------

    public List<DespachoResponseDTO> listarTodos() {
        return despachoRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public DespachoResponseDTO obtenerPorId(Long id) {
        return convertirAResponse(buscarDespachoOFallar(id));
    }

    public DespachoResponseDTO obtenerPorPedido(Long pedidoId) {
        Despacho despacho = despachoRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No hay despacho registrado para el pedido " + pedidoId));
        return convertirAResponse(despacho);
    }

    // ------------------------- Escrituras -------------------------

    /** Crea el despacho de un pedido, en estado PENDIENTE. */
    @Transactional
    public DespachoResponseDTO crear(DespachoRequestDTO dto) {
        if (despachoRepository.existsByPedidoId(dto.getPedidoId())) {
            throw new RuntimeException("El pedido " + dto.getPedidoId() + " ya tiene un despacho");
        }

        pedidoClient.buscarPedido(dto.getPedidoId())
                .orElseThrow(() -> new RuntimeException(
                        "El pedido " + dto.getPedidoId() + " no existe"));

        Despacho despacho = new Despacho();
        despacho.setPedidoId(dto.getPedidoId());
        despacho.setDireccionEnvio(dto.getDireccionEnvio());
        despacho.setSucursalOrigenId(dto.getSucursalOrigenId());
        despacho.setEstado(EstadoDespacho.PENDIENTE);
        despacho.setFechaCreacion(LocalDateTime.now());

        Despacho guardado = despachoRepository.save(despacho);
        log.info("Despacho creado: id={} pedido={} estado={}",
                guardado.getId(), guardado.getPedidoId(), guardado.getEstado());
        return convertirAResponse(guardado);
    }

    /**
     * Cambia el estado del despacho.
     * Regla: al pasar a ENTREGADO se guarda la fecha de entrega.
     */
    @Transactional
    public DespachoResponseDTO cambiarEstado(Long id, EstadoDespacho nuevoEstado) {
        Despacho despacho = buscarDespachoOFallar(id);
        despacho.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoDespacho.ENTREGADO) {
            despacho.setFechaEntrega(LocalDateTime.now());
        }

        Despacho actualizado = despachoRepository.save(despacho);
        log.info("Despacho {} cambio a estado {}", id, nuevoEstado);
        return convertirAResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        Despacho despacho = buscarDespachoOFallar(id);
        despachoRepository.delete(despacho);
        log.info("Despacho eliminado: id={}", id);
    }

    // ------------------------- Apoyo -------------------------

    private Despacho buscarDespachoOFallar(Long id) {
        return despachoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el despacho con id " + id));
    }

    private DespachoResponseDTO convertirAResponse(Despacho despacho) {
        DespachoResponseDTO dto = new DespachoResponseDTO();
        dto.setId(despacho.getId());
        dto.setPedidoId(despacho.getPedidoId());
        dto.setDireccionEnvio(despacho.getDireccionEnvio());
        dto.setSucursalOrigenId(despacho.getSucursalOrigenId());
        dto.setEstado(despacho.getEstado());
        dto.setFechaCreacion(despacho.getFechaCreacion());
        dto.setFechaEntrega(despacho.getFechaEntrega());
        return dto;
    }
}
