package com.bookpoint.clientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Datos que llegan para agregar una direccion a un cliente.
 */
@Data
public class DireccionRequestDTO {

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 150, message = "La calle no puede superar los 150 caracteres")
    private String calle;

    @Size(max = 10, message = "El numero no puede superar los 10 caracteres")
    private String numero;

    @NotBlank(message = "La comuna es obligatoria")
    private String comuna;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "La region es obligatoria")
    private String region;
}
