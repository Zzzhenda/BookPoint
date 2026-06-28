package com.bookpoint.sucursales.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada: son los datos que el cliente ENVIA para crear o actualizar
 * una sucursal. Lo separamos de la entidad para validar de forma limpia y para
 * no exponer la tabla directamente.
 *
 * Aqui viven las validaciones de Bean Validation (JSR 380).
 */
@Data
public class SucursalRequestDTO {

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
    private String nombre;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "La direccion es obligatoria")
    @Size(max = 200, message = "La direccion no puede superar los 200 caracteres")
    private String direccion;

    @Size(max = 20, message = "El telefono no puede superar los 20 caracteres")
    private String telefono;

    @Size(max = 100, message = "El horario no puede superar los 100 caracteres")
    private String horario;
}
