package com.bookpoint.productos;

import com.bookpoint.productos.dto.ProductoRequestDTO;
import com.bookpoint.productos.model.Producto;
import com.bookpoint.productos.model.TipoProducto;
import net.datafaker.Faker;

import java.math.BigDecimal;

/**
 * Fabrica de datos de prueba con DataFaker.
 */
public class TestDataFactory {

    private static final Faker faker = new Faker();

    /** Entidad Producto valida (un libro) con id asignado. */
    public static Producto unLibro() {
        Producto producto = new Producto();
        producto.setId(faker.number().numberBetween(1L, 1000L));
        producto.setTitulo(faker.book().title());
        producto.setAutor(faker.book().author());
        producto.setEditorial(faker.book().publisher());
        producto.setGenero(faker.book().genre());
        producto.setTipo(TipoProducto.LIBRO);
        producto.setPrecio(new BigDecimal("12990"));
        producto.setIsbn(faker.code().isbn13());
        producto.setActivo(true);
        return producto;
    }

    /** DTO de entrada valido para crear/actualizar un libro. */
    public static ProductoRequestDTO unRequestLibroValido() {
        ProductoRequestDTO dto = new ProductoRequestDTO();
        dto.setTitulo(faker.book().title());
        dto.setAutor(faker.book().author());
        dto.setEditorial(faker.book().publisher());
        dto.setGenero(faker.book().genre());
        dto.setTipo(TipoProducto.LIBRO);
        dto.setPrecio(new BigDecimal("9990"));
        dto.setIsbn(faker.code().isbn13());
        return dto;
    }
}
