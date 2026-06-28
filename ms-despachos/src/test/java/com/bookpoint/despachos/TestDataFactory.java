package com.bookpoint.despachos;

import com.bookpoint.despachos.dto.DespachoRequestDTO;
import com.bookpoint.despachos.dto.PedidoDTO;
import com.bookpoint.despachos.model.Despacho;
import com.bookpoint.despachos.model.EstadoDespacho;
import net.datafaker.Faker;

import java.time.LocalDateTime;

/**
 * Fabrica de datos de prueba con DataFaker.
 */
public class TestDataFactory {

    private static final Faker faker = new Faker();

    public static PedidoDTO unPedidoDTO() {
        PedidoDTO pedido = new PedidoDTO();
        pedido.setId(faker.number().numberBetween(1L, 100L));
        pedido.setClienteId(faker.number().numberBetween(1L, 100L));
        pedido.setEstado("CREADO");
        return pedido;
    }

    public static Despacho unDespacho() {
        Despacho despacho = new Despacho();
        despacho.setId(faker.number().numberBetween(1L, 1000L));
        despacho.setPedidoId(faker.number().numberBetween(1L, 100L));
        despacho.setDireccionEnvio(faker.address().fullAddress());
        despacho.setSucursalOrigenId(faker.number().numberBetween(1L, 3L));
        despacho.setEstado(EstadoDespacho.PENDIENTE);
        despacho.setFechaCreacion(LocalDateTime.now());
        return despacho;
    }

    public static DespachoRequestDTO unRequest(Long pedidoId) {
        DespachoRequestDTO dto = new DespachoRequestDTO();
        dto.setPedidoId(pedidoId);
        dto.setDireccionEnvio(faker.address().fullAddress());
        dto.setSucursalOrigenId(1L);
        return dto;
    }
}
