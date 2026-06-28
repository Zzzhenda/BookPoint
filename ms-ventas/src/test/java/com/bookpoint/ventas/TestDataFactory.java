package com.bookpoint.ventas;

import com.bookpoint.ventas.dto.PedidoDTO;
import com.bookpoint.ventas.dto.VentaRequestDTO;
import com.bookpoint.ventas.model.MetodoPago;
import net.datafaker.Faker;

import java.math.BigDecimal;

/**
 * Fabrica de datos de prueba con DataFaker.
 */
public class TestDataFactory {

    private static final Faker faker = new Faker();

    /** Pedido simulado tal como lo devolveria ms-pedidos. */
    public static PedidoDTO unPedidoDTO() {
        PedidoDTO pedido = new PedidoDTO();
        pedido.setId(faker.number().numberBetween(1L, 100L));
        pedido.setClienteId(faker.number().numberBetween(1L, 100L));
        pedido.setEstado("CREADO");
        pedido.setTotal(new BigDecimal("15000"));
        return pedido;
    }

    public static VentaRequestDTO unRequest(Long pedidoId) {
        VentaRequestDTO dto = new VentaRequestDTO();
        dto.setPedidoId(pedidoId);
        dto.setMetodoPago(MetodoPago.DEBITO);
        return dto;
    }
}
