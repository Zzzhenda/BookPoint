package com.bookpoint.sucursales;

import com.bookpoint.sucursales.dto.SucursalRequestDTO;
import com.bookpoint.sucursales.model.Sucursal;
import net.datafaker.Faker;

/**
 * Fabrica de datos de prueba.
 *
 * Usa DataFaker para generar valores falsos pero realistas y asi no repetir
 * datos a mano en cada test. Centraliza la creacion de objetos de prueba.
 */
public class TestDataFactory {

    private static final Faker faker = new Faker();

    /** Crea una entidad Sucursal valida con id ya asignado (como si viniera de la BD). */
    public static Sucursal unaSucursal() {
        Sucursal sucursal = new Sucursal();
        sucursal.setId(faker.number().numberBetween(1L, 1000L));
        sucursal.setNombre("BookPoint " + faker.company().name());
        sucursal.setCiudad("Concepcion");
        sucursal.setDireccion(faker.address().streetAddress());
        sucursal.setTelefono(faker.phoneNumber().cellPhone());
        sucursal.setHorario("Lun a Vie 9:00-19:00");
        sucursal.setActiva(true);
        return sucursal;
    }

    /** Crea un DTO de entrada valido para crear/actualizar una sucursal. */
    public static SucursalRequestDTO unRequestValido() {
        SucursalRequestDTO dto = new SucursalRequestDTO();
        dto.setNombre("BookPoint " + faker.company().name());
        dto.setCiudad("Temuco");
        dto.setDireccion(faker.address().streetAddress());
        dto.setTelefono(faker.phoneNumber().cellPhone());
        dto.setHorario("Lun a Sab 10:00-20:00");
        return dto;
    }
}
