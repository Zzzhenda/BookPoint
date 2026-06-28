package com.bookpoint.clientes.dto;

import lombok.Data;

import java.util.List;

/**
 * Datos de un cliente que devolvemos al exterior, incluyendo sus direcciones
 * ya transformadas a DTO.
 */
@Data
public class ClienteResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Boolean activo;
    private List<DireccionResponseDTO> direcciones;
}
