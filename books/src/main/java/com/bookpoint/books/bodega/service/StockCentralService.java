package com.bookpoint.books.bodega.service;

import com.bookpoint.books.bodega.dto.StockCentralDTO;
import com.bookpoint.books.bodega.model.StockCentral;
import com.bookpoint.books.bodega.repository.StockCentralRepository;
import com.bookpoint.books.exception.RecursoNoEncontradoException;
import com.bookpoint.books.exception.ReglaNegocioException;
import com.bookpoint.books.productos.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockCentralService {

    private static final Logger log = LoggerFactory.getLogger(StockCentralService.class);

    private final StockCentralRepository repository;
    private final ProductoRepository productoRepository;

    public StockCentralService(StockCentralRepository repository,
                               ProductoRepository productoRepository) {
        this.repository = repository;
        this.productoRepository = productoRepository;
    }

    public List<StockCentralDTO> listar() {
        log.info("Listando stock central");
        return repository.findAll().stream().map(this::aDTO).toList();
    }

    public StockCentralDTO buscarPorId(Long id) {
        return aDTO(repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("StockCentral con id " + id + " no encontrado")));
    }

    public StockCentralDTO crear(StockCentralDTO dto) {
        log.info("Creando stock central producto={}", dto.getProductoId());
        if (!productoRepository.existsById(dto.getProductoId())) {
            throw new RecursoNoEncontradoException("Producto " + dto.getProductoId() + " no existe");
        }
        if (repository.findByProductoId(dto.getProductoId()).isPresent()) {
            throw new ReglaNegocioException("Ya existe stock central para ese producto");
        }
        return aDTO(repository.save(aEntidad(dto)));
    }

    public StockCentralDTO actualizar(Long id, StockCentralDTO dto) {
        log.info("Actualizando stock central id {}", id);
        StockCentral s = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("StockCentral con id " + id + " no encontrado"));
        s.setCantidad(dto.getCantidad());
        s.setStockMinimo(dto.getStockMinimo());
        s.setUbicacion(dto.getUbicacion());
        return aDTO(repository.save(s));
    }

    public void eliminar(Long id) {
        log.info("Eliminando stock central id {}", id);
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("StockCentral con id " + id + " no encontrado");
        }
        repository.deleteById(id);
    }

    /** Incrementa el stock central (se llama desde el servicio de recepciones). */
    public void incrementar(Long productoId, int cantidad) {
        log.info("Incrementando {} unidades del producto {} en stock central", cantidad, productoId);
        StockCentral s = repository.findByProductoId(productoId).orElseGet(() ->
                repository.save(StockCentral.builder().productoId(productoId)
                        .cantidad(0).stockMinimo(0).ubicacion("SIN_ASIGNAR").build()));
        s.setCantidad(s.getCantidad() + cantidad);
        repository.save(s);
    }

    private StockCentralDTO aDTO(StockCentral s) {
        return StockCentralDTO.builder().id(s.getId()).productoId(s.getProductoId())
                .cantidad(s.getCantidad()).stockMinimo(s.getStockMinimo())
                .ubicacion(s.getUbicacion()).build();
    }

    private StockCentral aEntidad(StockCentralDTO d) {
        return StockCentral.builder().id(d.getId()).productoId(d.getProductoId())
                .cantidad(d.getCantidad()).stockMinimo(d.getStockMinimo())
                .ubicacion(d.getUbicacion()).build();
    }
}
