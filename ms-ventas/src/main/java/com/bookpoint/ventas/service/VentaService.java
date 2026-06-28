package com.bookpoint.ventas.service;

import com.bookpoint.ventas.client.PedidoClient;
import com.bookpoint.ventas.dto.PedidoDTO;
import com.bookpoint.ventas.dto.VentaRequestDTO;
import com.bookpoint.ventas.dto.VentaResponseDTO;
import com.bookpoint.ventas.exception.RecursoNoEncontradoException;
import com.bookpoint.ventas.model.Venta;
import com.bookpoint.ventas.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Logica de negocio de las ventas (boletas).
 *
 * Al registrar una venta, consulta a ms-pedidos para validar el pedido y
 * tomar su total; ademas impide facturar dos veces el mismo pedido.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final PedidoClient pedidoClient;

    // ------------------------- Lecturas -------------------------

    public List<VentaResponseDTO> listarTodas() {
        return ventaRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public VentaResponseDTO obtenerPorId(Long id) {
        return convertirAResponse(buscarVentaOFallar(id));
    }

    public VentaResponseDTO obtenerPorPedido(Long pedidoId) {
        Venta venta = ventaRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No hay venta registrada para el pedido " + pedidoId));
        return convertirAResponse(venta);
    }

    // ------------------------- Escrituras -------------------------

    /**
     * Registra la venta de un pedido.
     * 1) Valida que el pedido exista (ms-pedidos).
     * 2) Impide facturar dos veces el mismo pedido.
     * 3) Toma el monto del total del pedido y genera el numero de boleta.
     */
    @Transactional
    public VentaResponseDTO registrar(VentaRequestDTO dto) {
        if (ventaRepository.existsByPedidoId(dto.getPedidoId())) {
            throw new RuntimeException("El pedido " + dto.getPedidoId() + " ya tiene una venta registrada");
        }

        PedidoDTO pedido = pedidoClient.buscarPedido(dto.getPedidoId())
                .orElseThrow(() -> new RuntimeException(
                        "El pedido " + dto.getPedidoId() + " no existe"));

        Venta venta = new Venta();
        venta.setPedidoId(pedido.getId());
        venta.setMontoTotal(pedido.getTotal());
        venta.setMetodoPago(dto.getMetodoPago());
        venta.setFecha(LocalDateTime.now());
        venta.setNumeroBoleta(generarNumeroBoleta());

        Venta guardada = ventaRepository.save(venta);
        log.info("Venta registrada: boleta={} pedido={} monto={}",
                guardada.getNumeroBoleta(), guardada.getPedidoId(), guardada.getMontoTotal());
        return convertirAResponse(guardada);
    }

    @Transactional
    public void eliminar(Long id) {
        Venta venta = buscarVentaOFallar(id);
        ventaRepository.delete(venta);
        log.info("Venta eliminada: id={}", id);
    }

    // ------------------------- Apoyo -------------------------

    private Venta buscarVentaOFallar(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la venta con id " + id));
    }

    /** Genera un correlativo simple tipo BOL-000001 a partir de cuantas ventas hay. */
    private String generarNumeroBoleta() {
        long correlativo = ventaRepository.count() + 1;
        return String.format("BOL-%06d", correlativo);
    }

    private VentaResponseDTO convertirAResponse(Venta venta) {
        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setId(venta.getId());
        dto.setPedidoId(venta.getPedidoId());
        dto.setNumeroBoleta(venta.getNumeroBoleta());
        dto.setFecha(venta.getFecha());
        dto.setMontoTotal(venta.getMontoTotal());
        dto.setMetodoPago(venta.getMetodoPago());
        return dto;
    }
}
