package com.bookpoint.books.pedidos.service;

import com.bookpoint.books.clientes.repository.ClienteRepository;
import com.bookpoint.books.exception.RecursoNoEncontradoException;
import com.bookpoint.books.exception.ReglaNegocioException;
import com.bookpoint.books.inventario.service.StockService;
import com.bookpoint.books.pedidos.dto.DetallePedidoDTO;
import com.bookpoint.books.pedidos.dto.PedidoDTO;
import com.bookpoint.books.pedidos.model.DetallePedido;
import com.bookpoint.books.pedidos.model.EstadoPedido;
import com.bookpoint.books.pedidos.model.Pedido;
import com.bookpoint.books.pedidos.repository.PedidoRepository;
import com.bookpoint.books.productos.model.Producto;
import com.bookpoint.books.productos.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Logica de pedidos online.
 * Reglas de negocio:
 *  - Al crear, el pedido queda en estado PENDIENTE.
 *  - Al CONFIRMAR se descuenta el stock.
 *  - Solo se puede CANCELAR si esta PENDIENTE o CONFIRMADO.
 */
@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    private final PedidoRepository repository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final StockService stockService;

    public PedidoService(PedidoRepository repository,
                         ProductoRepository productoRepository,
                         ClienteRepository clienteRepository,
                         StockService stockService) {
        this.repository = repository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.stockService = stockService;
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> listar() {
        log.info("Listando pedidos");
        return repository.findAll().stream().map(this::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public PedidoDTO buscarPorId(Long id) {
        return aDTO(repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Pedido con id " + id + " no encontrado")));
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> buscarPorCliente(Long clienteId) {
        log.info("Listando pedidos del cliente {}", clienteId);
        return repository.findByClienteId(clienteId).stream().map(this::aDTO).toList();
    }

    @Transactional
    public PedidoDTO crear(PedidoDTO dto) {
        log.info("Creando pedido cliente={} sucursal={}", dto.getClienteId(), dto.getSucursalId());
        if (!clienteRepository.existsById(dto.getClienteId())) {
            throw new RecursoNoEncontradoException("Cliente " + dto.getClienteId() + " no existe");
        }

        Pedido p = Pedido.builder()
                .clienteId(dto.getClienteId())
                .sucursalId(dto.getSucursalId())
                .fecha(LocalDateTime.now())
                .estado(EstadoPedido.PENDIENTE)
                .total(BigDecimal.ZERO)
                .detalles(new ArrayList<>()).build();

        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedidoDTO d : dto.getDetalles()) {
            Producto prod = productoRepository.findById(d.getProductoId()).orElseThrow(() ->
                    new RecursoNoEncontradoException("Producto " + d.getProductoId() + " no existe"));
            BigDecimal subtotal = prod.getPrecio().multiply(BigDecimal.valueOf(d.getCantidad()));
            total = total.add(subtotal);
            p.getDetalles().add(DetallePedido.builder()
                    .productoId(d.getProductoId()).cantidad(d.getCantidad())
                    .precioUnitario(prod.getPrecio()).subtotal(subtotal).pedido(p).build());
        }
        p.setTotal(total);
        return aDTO(repository.save(p));
    }

    @Transactional
    public PedidoDTO confirmar(Long id) {
        log.info("Confirmando pedido id {}", id);
        Pedido p = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Pedido con id " + id + " no encontrado"));
        if (p.getEstado() != EstadoPedido.PENDIENTE) {
            throw new ReglaNegocioException(
                    "Solo se pueden confirmar pedidos PENDIENTES. Estado actual: " + p.getEstado());
        }
        // Descontar stock por cada item.
        for (DetallePedido d : p.getDetalles()) {
            stockService.descontar(d.getProductoId(), p.getSucursalId(), d.getCantidad());
        }
        p.setEstado(EstadoPedido.CONFIRMADO);
        return aDTO(repository.save(p));
    }

    @Transactional
    public PedidoDTO cancelar(Long id) {
        log.info("Cancelando pedido id {}", id);
        Pedido p = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Pedido con id " + id + " no encontrado"));
        if (p.getEstado() != EstadoPedido.PENDIENTE && p.getEstado() != EstadoPedido.CONFIRMADO) {
            throw new ReglaNegocioException("No se puede cancelar un pedido en estado " + p.getEstado());
        }
        p.setEstado(EstadoPedido.CANCELADO);
        return aDTO(repository.save(p));
    }

    @Transactional
    public PedidoDTO cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        log.info("Cambiando estado del pedido {} a {}", id, nuevoEstado);
        Pedido p = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Pedido con id " + id + " no encontrado"));
        p.setEstado(nuevoEstado);
        return aDTO(repository.save(p));
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando pedido id {}", id);
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Pedido con id " + id + " no encontrado");
        }
        repository.deleteById(id);
    }

    private PedidoDTO aDTO(Pedido p) {
        List<DetallePedidoDTO> ds = new ArrayList<>();
        if (p.getDetalles() != null) {
            for (DetallePedido d : p.getDetalles()) {
                ds.add(DetallePedidoDTO.builder().id(d.getId()).productoId(d.getProductoId())
                        .cantidad(d.getCantidad()).precioUnitario(d.getPrecioUnitario())
                        .subtotal(d.getSubtotal()).build());
            }
        }
        return PedidoDTO.builder().id(p.getId()).clienteId(p.getClienteId())
                .sucursalId(p.getSucursalId()).fecha(p.getFecha()).total(p.getTotal())
                .estado(p.getEstado()).detalles(ds).build();
    }
}
