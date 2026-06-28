package com.bookpoint.carrito.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Datos para crear un carrito nuevo: basta con el cliente dueno.
 */
@Data
public class CarritoRequestDTO {

    @NotNull(message = "El id del cliente es obligatorio")
    private Long clienteId;
}
