package com.bookpoint.despachos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Datos para crear un despacho: de que pedido, a que direccion y desde que sucursal.
 */
@Data
public class DespachoRequestDTO {

    @NotNull(message = "El id del pedido es obligatorio")
    private Long pedidoId;

    @NotBlank(message = "La direccion de envio es obligatoria")
    @Size(max = 200, message = "La direccion no puede superar los 200 caracteres")
    private String direccionEnvio;

    @NotNull(message = "El id de la sucursal de origen es obligatorio")
    private Long sucursalOrigenId;
}
