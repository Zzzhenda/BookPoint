package com.bookpoint.clientes.dto;

import lombok.Data;

/**
 * Datos de una direccion que devolvemos al cliente.
 * No incluye el Cliente para evitar referencias circulares en el JSON.
 */
@Data
public class DireccionResponseDTO {

    private Long id;
    private String calle;
    private String numero;
    private String comuna;
    private String ciudad;
    private String region;
}
