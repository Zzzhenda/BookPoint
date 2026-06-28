package com.bookpoint.productos.service;

import com.bookpoint.productos.dto.ProductoRequestDTO;
import com.bookpoint.productos.dto.ProductoResponseDTO;
import com.bookpoint.productos.exception.RecursoNoEncontradoException;
import com.bookpoint.productos.model.Producto;
import com.bookpoint.productos.model.TipoProducto;
import com.bookpoint.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Logica de negocio del catalogo de productos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    // ------------------------- Lecturas -------------------------

    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public ProductoResponseDTO obtenerPorId(Long id) {
        return convertirAResponse(buscarProductoOFallar(id));
    }

    public List<ProductoResponseDTO> buscarPorAutor(String autor) {
        return productoRepository.findByAutorIgnoreCase(autor).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<ProductoResponseDTO> buscarPorGenero(String genero) {
        return productoRepository.findByGeneroIgnoreCase(genero).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public List<ProductoResponseDTO> buscarPorPrecioMaximo(BigDecimal precioMaximo) {
        return productoRepository.findByPrecioLessThanEqual(precioMaximo).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // ------------------------- Escrituras -------------------------

    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        validarReglas(dto);

        Producto producto = new Producto();
        copiarDatos(dto, producto);
        producto.setActivo(true);

        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado: '{}' ({}) (id={})",
                guardado.getTitulo(), guardado.getTipo(), guardado.getId());
        return convertirAResponse(guardado);
    }

    @Transactional
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto producto = buscarProductoOFallar(id);
        validarReglas(dto);

        copiarDatos(dto, producto);
        Producto actualizado = productoRepository.save(producto);
        log.info("Producto actualizado: id={}", actualizado.getId());
        return convertirAResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = buscarProductoOFallar(id);
        productoRepository.delete(producto);
        log.info("Producto eliminado: id={}", id);
    }

    // ------------------------- Apoyo -------------------------

    private Producto buscarProductoOFallar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el producto con id " + id));
    }

    /** Regla de negocio: un libro siempre debe tener autor. */
    private void validarReglas(ProductoRequestDTO dto) {
        boolean esLibroSinAutor = dto.getTipo() == TipoProducto.LIBRO
                && (dto.getAutor() == null || dto.getAutor().isBlank());
        if (esLibroSinAutor) {
            throw new RuntimeException("Un producto de tipo LIBRO debe tener autor");
        }
    }

    private void copiarDatos(ProductoRequestDTO dto, Producto producto) {
        producto.setTitulo(dto.getTitulo());
        producto.setAutor(dto.getAutor());
        producto.setEditorial(dto.getEditorial());
        producto.setGenero(dto.getGenero());
        producto.setTipo(dto.getTipo());
        producto.setPrecio(dto.getPrecio());
        producto.setIsbn(dto.getIsbn());
    }

    private ProductoResponseDTO convertirAResponse(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setTitulo(producto.getTitulo());
        dto.setAutor(producto.getAutor());
        dto.setEditorial(producto.getEditorial());
        dto.setGenero(producto.getGenero());
        dto.setTipo(producto.getTipo());
        dto.setPrecio(producto.getPrecio());
        dto.setIsbn(producto.getIsbn());
        dto.setActivo(producto.getActivo());
        return dto;
    }
}
