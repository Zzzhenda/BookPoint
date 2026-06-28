package com.bookpoint.sucursales.dto;

import lombok.Data;

/**
 * DTO de salida: son los datos que el microservicio DEVUELVE al cliente.
 * Lo usamos para controlar exactamente que campos viajan en el JSON de respuesta.
 */
@Data
public class SucursalResponseDTO {

    private Long id;
    private String nombre;
    private String ciudad;
    private String direccion;
    private String telefono;
    private String horario;
    private Boolean activa;
}
