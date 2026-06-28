package com.bookpoint.carrito.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Datos para agregar un item al carrito: que producto y cuantas unidades.
 * El titulo y el precio NO llegan del cliente: se obtienen de ms-productos.
 */
@Data
public class ItemRequestDTO {

    @NotNull(message = "El id del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}
