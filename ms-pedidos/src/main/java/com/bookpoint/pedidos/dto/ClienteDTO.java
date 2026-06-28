package com.bookpoint.pedidos.dto;

import lombok.Data;

/**
 * Datos del cliente que nos interesan de ms-clientes (para validar que existe).
 */
@Data
public class ClienteDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
}
