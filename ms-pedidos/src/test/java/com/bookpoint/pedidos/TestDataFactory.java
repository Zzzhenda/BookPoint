package com.bookpoint.pedidos;

import com.bookpoint.pedidos.dto.CarritoDTO;
import com.bookpoint.pedidos.dto.ClienteDTO;
import com.bookpoint.pedidos.dto.ItemCarritoDTO;
import com.bookpoint.pedidos.dto.PedidoRequestDTO;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.util.List;

/**
 * Fabrica de datos de prueba con DataFaker.
 */
public class TestDataFactory {

    private static final Faker faker = new Faker();

    public static ClienteDTO unClienteDTO() {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(faker.number().numberBetween(1L, 100L));
        cliente.setNombre(faker.name().firstName());
        cliente.setApellido(faker.name().lastName());
        cliente.setEmail(faker.internet().emailAddress());
        return cliente;
    }

    /** Carrito simulado con un item, tal como lo devolveria ms-carrito. */
    public static CarritoDTO unCarritoConUnItem(Long clienteId) {
        ItemCarritoDTO item = new ItemCarritoDTO();
        item.setProductoId(faker.number().numberBetween(1L, 50L));
        item.setTituloProducto(faker.book().title());
        item.setPrecioUnitario(new BigDecimal("1000"));
        item.setCantidad(2);
        item.setSubtotal(new BigDecimal("2000"));

        CarritoDTO carrito = new CarritoDTO();
        carrito.setId(faker.number().numberBetween(1L, 100L));
        carrito.setClienteId(clienteId);
        carrito.setItems(List.of(item));
        carrito.setTotal(new BigDecimal("2000"));
        return carrito;
    }

    public static PedidoRequestDTO unRequest(Long clienteId, Long carritoId) {
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setClienteId(clienteId);
        dto.setCarritoId(carritoId);
        return dto;
    }
}
