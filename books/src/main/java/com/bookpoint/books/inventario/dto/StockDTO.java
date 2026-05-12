package com.bookpoint.books.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDTO {

    private Long id;

    @NotNull(message = "El id del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "El id de la sucursal es obligatorio")
    private Long sucursalId;

    @NotNull
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    @NotNull
    @Min(value = 0)
    private Integer stockMinimo;
}
