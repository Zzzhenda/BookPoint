package com.bookpoint.books.ventas.service;

import com.bookpoint.books.exception.RecursoNoEncontradoException;
import com.bookpoint.books.inventario.service.StockService;
import com.bookpoint.books.productos.model.Producto;
import com.bookpoint.books.productos.repository.ProductoRepository;
import com.bookpoint.books.sucursales.repository.SucursalRepository;
import com.bookpoint.books.ventas.dto.DetalleVentaDTO;
import com.bookpoint.books.ventas.dto.VentaDTO;
import com.bookpoint.books.ventas.model.DetalleVenta;
import com.bookpoint.books.ventas.model.Venta;
import com.bookpoint.books.ventas.repository.VentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Logica de ventas en caja.
 * Reglas de negocio:
 *  - El precio unitario se lee del catalogo (no se confia en el cliente).
 *  - El total se calcula sumando subtotales.
 *  - Al registrar la venta se descuenta el stock de la sucursal.
 */
@Service
public class VentaService {

    private static final Logger log = LoggerFactory.getLogger(VentaService.class);

    private final VentaRepository repository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final StockService stockService;

    public VentaService(VentaRepository repository,
                        ProductoRepository productoRepository,
                        SucursalRepository sucursalRepository,
                        StockService stockService) {
        this.repository = repository;
        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
        this.stockService = stockService;
    }

    @Transactional(readOnly = true)
    public List<VentaDTO> listar() {
        log.info("Listando ventas");
        return repository.findAll().stream().map(this::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public VentaDTO buscarPorId(Long id) {
        return aDTO(repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Venta con id " + id + " no encontrada")));
    }

    @Transactional
    public VentaDTO crear(VentaDTO dto) {
        log.info("Registrando venta en sucursal {} con {} items",
                dto.getSucursalId(), dto.getDetalles().size());

        if (!sucursalRepository.existsById(dto.getSucursalId())) {
            throw new RecursoNoEncontradoException("Sucursal " + dto.getSucursalId() + " no existe");
        }

        Venta venta = Venta.builder()
                .sucursalId(dto.getSucursalId())
                .clienteId(dto.getClienteId())
                .fecha(LocalDateTime.now())
                .tipoDocumento(dto.getTipoDocumento())
                .detalles(new ArrayList<>())
                .total(BigDecimal.ZERO).build();

        BigDecimal total = BigDecimal.ZERO;
        for (DetalleVentaDTO d : dto.getDetalles()) {
            Producto p = productoRepository.findById(d.getProductoId()).orElseThrow(() ->
                    new RecursoNoEncontradoException("Producto " + d.getProductoId() + " no existe"));
            BigDecimal subtotal = p.getPrecio().multiply(BigDecimal.valueOf(d.getCantidad()));
            total = total.add(subtotal);
            venta.getDetalles().add(DetalleVenta.builder()
                    .productoId(d.getProductoId()).cantidad(d.getCantidad())
                    .precioUnitario(p.getPrecio()).subtotal(subtotal).venta(venta).build());
        }
        venta.setTotal(total);

        // Descontar stock por cada item vendido (regla del PDF).
        for (DetalleVenta det : venta.getDetalles()) {
            stockService.descontar(det.getProductoId(), venta.getSucursalId(), det.getCantidad());
        }

        Venta guardada = repository.save(venta);
        log.info("Venta {} registrada por {}", guardada.getId(), guardada.getTotal());
        return aDTO(guardada);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando venta id {}", id);
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Venta con id " + id + " no encontrada");
        }
        repository.deleteById(id);
    }

    private VentaDTO aDTO(Venta v) {
        List<DetalleVentaDTO> ds = new ArrayList<>();
        if (v.getDetalles() != null) {
            for (DetalleVenta d : v.getDetalles()) {
                ds.add(DetalleVentaDTO.builder().id(d.getId()).productoId(d.getProductoId())
                        .cantidad(d.getCantidad()).precioUnitario(d.getPrecioUnitario())
                        .subtotal(d.getSubtotal()).build());
            }
        }
        return VentaDTO.builder().id(v.getId()).sucursalId(v.getSucursalId())
                .clienteId(v.getClienteId()).fecha(v.getFecha()).total(v.getTotal())
                .tipoDocumento(v.getTipoDocumento()).detalles(ds).build();
    }
}
