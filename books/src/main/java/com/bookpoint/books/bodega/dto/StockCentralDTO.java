package com.bookpoint.books.bodega.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockCentralDTO {

    private Long id;

    @NotNull(message = "El id del producto es obligatorio")
    private Long productoId;

    @NotNull
    @Min(value = 0)
    private Integer cantidad;

    @NotNull
    @Min(value = 0)
    private Integer stockMinimo;

    @Size(max = 50)
    private String ubicacion;
}
