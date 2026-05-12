package com.bookpoint.books.productos.service;

import com.bookpoint.books.exception.RecursoNoEncontradoException;
import com.bookpoint.books.productos.dto.ProductoDTO;
import com.bookpoint.books.productos.model.Producto;
import com.bookpoint.books.productos.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Logica de negocio del catalogo de productos.
 * Cubre el patron CSR (controller-service-repository).
 */
@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<ProductoDTO> listar() {
        log.info("Listando todos los productos");
        return repository.findAll().stream().map(this::aDTO).toList();
    }

    public ProductoDTO buscarPorId(Long id) {
        log.info("Buscando producto con id {}", id);
        Producto p = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Producto con id " + id + " no encontrado"));
        return aDTO(p);
    }

    public List<ProductoDTO> buscarPorAutor(String autor) {
        log.info("Buscando productos por autor: {}", autor);
        return repository.findByAutorContainingIgnoreCase(autor)
                .stream().map(this::aDTO).toList();
    }

    public List<ProductoDTO> buscarPorEditorial(String editorial) {
        log.info("Buscando productos por editorial: {}", editorial);
        return repository.findByEditorialContainingIgnoreCase(editorial)
                .stream().map(this::aDTO).toList();
    }

    public List<ProductoDTO> buscarPorGenero(String genero) {
        log.info("Buscando productos por genero: {}", genero);
        return repository.findByGeneroContainingIgnoreCase(genero)
                .stream().map(this::aDTO).toList();
    }

    public ProductoDTO crear(ProductoDTO dto) {
        log.info("Creando producto: {}", dto.getTitulo());
        Producto p = aEntidad(dto);
        if (p.getActivo() == null) p.setActivo(true);
        Producto guardado = repository.save(p);
        log.info("Producto creado con id {}", guardado.getId());
        return aDTO(guardado);
    }

    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        log.info("Actualizando producto id {}", id);
        Producto p = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Producto con id " + id + " no encontrado"));
        p.setTitulo(dto.getTitulo());
        p.setAutor(dto.getAutor());
        p.setEditorial(dto.getEditorial());
        p.setGenero(dto.getGenero());
        p.setPrecio(dto.getPrecio());
        if (dto.getActivo() != null) p.setActivo(dto.getActivo());
        return aDTO(repository.save(p));
    }

    public void eliminar(Long id) {
        log.info("Eliminando producto id {}", id);
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Producto con id " + id + " no encontrado");
        }
        repository.deleteById(id);
    }

    // Conversion entidad <-> DTO

    private ProductoDTO aDTO(Producto p) {
        return ProductoDTO.builder()
                .id(p.getId()).titulo(p.getTitulo()).autor(p.getAutor())
                .editorial(p.getEditorial()).genero(p.getGenero())
                .precio(p.getPrecio()).activo(p.getActivo()).build();
    }

    private Producto aEntidad(ProductoDTO d) {
        return Producto.builder()
                .id(d.getId()).titulo(d.getTitulo()).autor(d.getAutor())
                .editorial(d.getEditorial()).genero(d.getGenero())
                .precio(d.getPrecio()).activo(d.getActivo()).build();
    }
}
