package com.bookpoint.pedidos.service;

import com.bookpoint.pedidos.client.CarritoClient;
import com.bookpoint.pedidos.client.ClienteClient;
import com.bookpoint.pedidos.dto.CarritoDTO;
import com.bookpoint.pedidos.dto.DetalleResponseDTO;
import com.bookpoint.pedidos.dto.ItemCarritoDTO;
import com.bookpoint.pedidos.dto.PedidoRequestDTO;
import com.bookpoint.pedidos.dto.PedidoResponseDTO;
import com.bookpoint.pedidos.exception.RecursoNoEncontradoException;
import com.bookpoint.pedidos.model.DetallePedido;
import com.bookpoint.pedidos.model.EstadoPedido;
import com.bookpoint.pedidos.model.Pedido;
import com.bookpoint.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Logica de negocio de los pedidos.
 *
 * El metodo clave es crear(): valida el cliente en ms-clientes, trae el
 * carrito desde ms-carrito y arma el pedido con sus detalles y total.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteClient clienteClient;
    private final CarritoClient carritoClient;

    // ------------------------- Lecturas -------------------------

    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public PedidoResponseDTO obtenerPorId(Long id) {
        return convertirAResponse(buscarPedidoOFallar(id));
    }

    public List<PedidoResponseDTO> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // ------------------------- Escrituras -------------------------

    /**
     * Genera un pedido a partir de un carrito.
     * 1) Valida que el cliente exista (ms-clientes).
     * 2) Trae el carrito (ms-carrito) y verifica que tenga items.
     * 3) Copia cada item del carrito como un detalle del pedido.
     * 4) Calcula el total como la suma de los subtotales.
     */
    @Transactional
    public PedidoResponseDTO crear(PedidoRequestDTO dto) {
        // 1) Validar cliente
        clienteClient.buscarCliente(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException(
                        "El cliente " + dto.getClienteId() + " no existe"));

        // 2) Traer carrito
        CarritoDTO carrito = carritoClient.obtenerCarrito(dto.getCarritoId())
                .orElseThrow(() -> new RuntimeException(
                        "El carrito " + dto.getCarritoId() + " no existe"));

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new RuntimeException("No se puede generar un pedido con un carrito vacio");
        }

        // 3) Armar el pedido con sus detalles
        Pedido pedido = new Pedido();
        pedido.setClienteId(dto.getClienteId());
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.CREADO);

        for (ItemCarritoDTO item : carrito.getItems()) {
            DetallePedido detalle = new DetallePedido();
            detalle.setProductoId(item.getProductoId());
            detalle.setTituloProducto(item.getTituloProducto());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detalle.setCantidad(item.getCantidad());
            detalle.setSubtotal(item.getSubtotal());
            pedido.agregarDetalle(detalle);
        }

        // 4) Total = suma de subtotales
        BigDecimal total = pedido.getDetalles().stream()
                .map(DetallePedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.save(pedido);
        log.info("Pedido creado: id={} cliente={} total={}",
                guardado.getId(), guardado.getClienteId(), guardado.getTotal());
        return convertirAResponse(guardado);
    }

    /** Cambia el estado del pedido (CREADO, PAGADO, DESPACHADO, ANULADO). */
    @Transactional
    public PedidoResponseDTO cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPedidoOFallar(id);
        pedido.setEstado(nuevoEstado);
        Pedido actualizado = pedidoRepository.save(pedido);
        log.info("Pedido {} cambio a estado {}", id, nuevoEstado);
        return convertirAResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        Pedido pedido = buscarPedidoOFallar(id);
        pedidoRepository.delete(pedido);
        log.info("Pedido eliminado: id={}", id);
    }

    // ------------------------- Apoyo -------------------------

    private Pedido buscarPedidoOFallar(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el pedido con id " + id));
    }

    private PedidoResponseDTO convertirAResponse(Pedido pedido) {
        List<DetalleResponseDTO> detalles = pedido.getDetalles().stream()
                .map(this::convertirDetalle)
                .toList();

        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setClienteId(pedido.getClienteId());
        dto.setFecha(pedido.getFecha());
        dto.setEstado(pedido.getEstado());
        dto.setTotal(pedido.getTotal());
        dto.setDetalles(detalles);
        return dto;
    }

    private DetalleResponseDTO convertirDetalle(DetallePedido detalle) {
        DetalleResponseDTO dto = new DetalleResponseDTO();
        dto.setId(detalle.getId());
        dto.setProductoId(detalle.getProductoId());
        dto.setTituloProducto(detalle.getTituloProducto());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setCantidad(detalle.getCantidad());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}
