package com.bookpoint.books.bodega.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleRecepcionDTO {

    private Long id;

    @NotNull
    private Long productoId;

    @NotNull
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;
}
