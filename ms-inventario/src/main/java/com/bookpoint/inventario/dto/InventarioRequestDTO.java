package com.bookpoint.inventario.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

/**
 * Datos que llegan para registrar o actualizar el stock de un producto
 * en una sucursal.
 */
@Data
public class InventarioRequestDTO {

    @NotNull(message = "El id del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "El id de la sucursal es obligatorio")
    private Long sucursalId;

    @NotNull(message = "La cantidad es obligatoria")
    @PositiveOrZero(message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    @NotNull(message = "El stock minimo es obligatorio")
    @PositiveOrZero(message = "El stock minimo no puede ser negativo")
    private Integer stockMinimo;
}
