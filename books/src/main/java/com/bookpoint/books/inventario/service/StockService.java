package com.bookpoint.books.inventario.service;

import com.bookpoint.books.exception.RecursoNoEncontradoException;
import com.bookpoint.books.exception.ReglaNegocioException;
import com.bookpoint.books.inventario.dto.StockDTO;
import com.bookpoint.books.inventario.model.Stock;
import com.bookpoint.books.inventario.repository.StockRepository;
import com.bookpoint.books.productos.repository.ProductoRepository;
import com.bookpoint.books.sucursales.repository.SucursalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Logica de inventario. Valida que el producto y la sucursal existan
 * antes de crear stock. Aplica la regla de stock no negativo al descontar.
 */
@Service
public class StockService {

    private static final Logger log = LoggerFactory.getLogger(StockService.class);

    private final StockRepository repository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    public StockService(StockRepository repository,
                        ProductoRepository productoRepository,
                        SucursalRepository sucursalRepository) {
        this.repository = repository;
        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
    }

    public List<StockDTO> listar() {
        log.info("Listando stock");
        return repository.findAll().stream().map(this::aDTO).toList();
    }

    public StockDTO buscarPorId(Long id) {
        return aDTO(repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Stock con id " + id + " no encontrado")));
    }

    public List<StockDTO> buscarPorSucursal(Long sucursalId) {
        log.info("Listando stock de la sucursal {}", sucursalId);
        return repository.findBySucursalId(sucursalId).stream().map(this::aDTO).toList();
    }

    public StockDTO crear(StockDTO dto) {
        log.info("Creando stock producto={} sucursal={}", dto.getProductoId(), dto.getSucursalId());
        if (!productoRepository.existsById(dto.getProductoId())) {
            throw new RecursoNoEncontradoException("Producto " + dto.getProductoId() + " no existe");
        }
        if (!sucursalRepository.existsById(dto.getSucursalId())) {
            throw new RecursoNoEncontradoException("Sucursal " + dto.getSucursalId() + " no existe");
        }
        if (repository.findByProductoIdAndSucursalId(dto.getProductoId(), dto.getSucursalId()).isPresent()) {
            throw new ReglaNegocioException("Ya existe stock para ese producto en esa sucursal");
        }
        return aDTO(repository.save(aEntidad(dto)));
    }

    public StockDTO actualizar(Long id, StockDTO dto) {
        log.info("Actualizando stock id {}", id);
        Stock s = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Stock con id " + id + " no encontrado"));
        s.setCantidad(dto.getCantidad());
        s.setStockMinimo(dto.getStockMinimo());
        return aDTO(repository.save(s));
    }

    /**
     * Descuenta unidades del stock. Lo usan ventas y pedidos al confirmarse.
     * Regla de negocio: no se puede dejar el stock en negativo.
     */
    public void descontar(Long productoId, Long sucursalId, int cantidad) {
        log.info("Descontando {} unidades del producto {} en sucursal {}",
                cantidad, productoId, sucursalId);
        Stock s = repository.findByProductoIdAndSucursalId(productoId, sucursalId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No hay stock para producto " + productoId + " en sucursal " + sucursalId));
        if (s.getCantidad() < cantidad) {
            throw new ReglaNegocioException(
                    "Stock insuficiente. Disponible: " + s.getCantidad() + ", solicitado: " + cantidad);
        }
        s.setCantidad(s.getCantidad() - cantidad);
        repository.save(s);
    }

    public void eliminar(Long id) {
        log.info("Eliminando stock id {}", id);
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Stock con id " + id + " no encontrado");
        }
        repository.deleteById(id);
    }

    private StockDTO aDTO(Stock s) {
        return StockDTO.builder().id(s.getId()).productoId(s.getProductoId())
                .sucursalId(s.getSucursalId()).cantidad(s.getCantidad())
                .stockMinimo(s.getStockMinimo()).build();
    }

    private Stock aEntidad(StockDTO d) {
        return Stock.builder().id(d.getId()).productoId(d.getProductoId())
                .sucursalId(d.getSucursalId()).cantidad(d.getCantidad())
                .stockMinimo(d.getStockMinimo()).build();
    }
}
