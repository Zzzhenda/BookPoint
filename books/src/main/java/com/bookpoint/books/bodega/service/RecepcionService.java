package com.bookpoint.books.bodega.service;

import com.bookpoint.books.bodega.dto.DetalleRecepcionDTO;
import com.bookpoint.books.bodega.dto.RecepcionDTO;
import com.bookpoint.books.bodega.model.DetalleRecepcion;
import com.bookpoint.books.bodega.model.Recepcion;
import com.bookpoint.books.bodega.repository.RecepcionRepository;
import com.bookpoint.books.exception.RecursoNoEncontradoException;
import com.bookpoint.books.proveedores.repository.ProveedorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Logica de recepcion de mercaderia. Al registrar una recepcion se
 * incrementa automaticamente el stock central (regla de negocio del PDF).
 */
@Service
public class RecepcionService {

    private static final Logger log = LoggerFactory.getLogger(RecepcionService.class);

    private final RecepcionRepository repository;
    private final StockCentralService stockCentralService;
    private final ProveedorRepository proveedorRepository;

    public RecepcionService(RecepcionRepository repository,
                            StockCentralService stockCentralService,
                            ProveedorRepository proveedorRepository) {
        this.repository = repository;
        this.stockCentralService = stockCentralService;
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional(readOnly = true)
    public List<RecepcionDTO> listar() {
        log.info("Listando recepciones");
        return repository.findAll().stream().map(this::aDTO).toList();
    }

    @Transactional(readOnly = true)
    public RecepcionDTO buscarPorId(Long id) {
        return aDTO(repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Recepcion con id " + id + " no encontrada")));
    }

    @Transactional
    public RecepcionDTO crear(RecepcionDTO dto) {
        log.info("Registrando recepcion del proveedor {}", dto.getProveedorId());
        if (!proveedorRepository.existsById(dto.getProveedorId())) {
            throw new RecursoNoEncontradoException("Proveedor " + dto.getProveedorId() + " no existe");
        }
        Recepcion r = Recepcion.builder()
                .proveedorId(dto.getProveedorId())
                .fecha(dto.getFecha())
                .observaciones(dto.getObservaciones())
                .detalles(new ArrayList<>()).build();
        for (DetalleRecepcionDTO d : dto.getDetalles()) {
            r.getDetalles().add(DetalleRecepcion.builder()
                    .productoId(d.getProductoId()).cantidad(d.getCantidad()).recepcion(r).build());
        }
        Recepcion guardada = repository.save(r);
        for (DetalleRecepcion d : guardada.getDetalles()) {
            stockCentralService.incrementar(d.getProductoId(), d.getCantidad());
        }
        return aDTO(guardada);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando recepcion id {}", id);
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Recepcion con id " + id + " no encontrada");
        }
        repository.deleteById(id);
    }

    private RecepcionDTO aDTO(Recepcion r) {
        List<DetalleRecepcionDTO> ds = new ArrayList<>();
        if (r.getDetalles() != null) {
            for (DetalleRecepcion d : r.getDetalles()) {
                ds.add(DetalleRecepcionDTO.builder().id(d.getId())
                        .productoId(d.getProductoId()).cantidad(d.getCantidad()).build());
            }
        }
        return RecepcionDTO.builder().id(r.getId()).proveedorId(r.getProveedorId())
                .fecha(r.getFecha()).observaciones(r.getObservaciones()).detalles(ds).build();
    }
}
