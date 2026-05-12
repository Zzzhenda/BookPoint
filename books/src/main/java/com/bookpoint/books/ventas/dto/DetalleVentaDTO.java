package com.bookpoint.books.ventas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVentaDTO {

    private Long id;

    @NotNull
    private Long productoId;

    @NotNull
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;

    /** Calculados por el servidor, no se confia en el cliente. */
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
