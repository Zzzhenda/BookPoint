package com.bookpoint.inventario;

import com.bookpoint.inventario.dto.InventarioRequestDTO;
import com.bookpoint.inventario.model.Inventario;
import net.datafaker.Faker;

/**
 * Fabrica de datos de prueba con DataFaker.
 */
public class TestDataFactory {

    private static final Faker faker = new Faker();

    /** Entidad Inventario valida con id asignado. */
    public static Inventario unInventario() {
        Inventario inventario = new Inventario();
        inventario.setId(faker.number().numberBetween(1L, 1000L));
        inventario.setProductoId(faker.number().numberBetween(1L, 50L));
        inventario.setSucursalId(faker.number().numberBetween(1L, 3L));
        inventario.setCantidad(20);
        inventario.setStockMinimo(5);
        return inventario;
    }

    /** DTO de entrada valido para registrar stock. */
    public static InventarioRequestDTO unRequestValido() {
        InventarioRequestDTO dto = new InventarioRequestDTO();
        dto.setProductoId(faker.number().numberBetween(1L, 50L));
        dto.setSucursalId(faker.number().numberBetween(1L, 3L));
        dto.setCantidad(15);
        dto.setStockMinimo(5);
        return dto;
    }
}
