package com.bookpoint.clientes;

import com.bookpoint.clientes.dto.ClienteRequestDTO;
import com.bookpoint.clientes.model.Cliente;
import net.datafaker.Faker;

/**
 * Fabrica de datos de prueba con DataFaker.
 */
public class TestDataFactory {

    private static final Faker faker = new Faker();

    /** Entidad Cliente valida con id asignado (como si viniera de la BD). */
    public static Cliente unCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(faker.number().numberBetween(1L, 1000L));
        cliente.setNombre(faker.name().firstName());
        cliente.setApellido(faker.name().lastName());
        cliente.setEmail(faker.internet().emailAddress());
        cliente.setTelefono(faker.phoneNumber().cellPhone());
        cliente.setActivo(true);
        return cliente;
    }

    /** DTO de entrada valido para crear/actualizar un cliente. */
    public static ClienteRequestDTO unRequestValido() {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNombre(faker.name().firstName());
        dto.setApellido(faker.name().lastName());
        dto.setEmail(faker.internet().emailAddress());
        dto.setTelefono(faker.phoneNumber().cellPhone());
        return dto;
    }
}
