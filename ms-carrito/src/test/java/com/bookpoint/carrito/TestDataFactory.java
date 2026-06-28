package com.bookpoint.carrito;

import com.bookpoint.carrito.dto.ItemRequestDTO;
import com.bookpoint.carrito.dto.ProductoDTO;
import com.bookpoint.carrito.model.Carrito;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Fabrica de datos de prueba con DataFaker.
 */
public class TestDataFactory {

    private static final Faker faker = new Faker();

    /** Carrito vacio con id asignado. */
    public static Carrito unCarrito() {
        Carrito carrito = new Carrito();
        carrito.setId(faker.number().numberBetween(1L, 1000L));
        carrito.setClienteId(faker.number().numberBetween(1L, 100L));
        carrito.setFechaCreacion(LocalDateTime.now());
        return carrito;
    }

    /** Producto simulado tal como lo devolveria ms-productos. */
    public static ProductoDTO unProductoDTO() {
        ProductoDTO producto = new ProductoDTO();
        producto.setId(faker.number().numberBetween(1L, 50L));
        producto.setTitulo(faker.book().title());
        producto.setPrecio(new BigDecimal("9990"));
        return producto;
    }

    /** Peticion para agregar un item. */
    public static ItemRequestDTO unItemRequest(Long productoId, int cantidad) {
        ItemRequestDTO dto = new ItemRequestDTO();
        dto.setProductoId(productoId);
        dto.setCantidad(cantidad);
        return dto;
    }
}
