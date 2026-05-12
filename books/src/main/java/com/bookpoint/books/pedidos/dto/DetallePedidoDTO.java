package com.bookpoint.books.pedidos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoDTO {

    private Long id;

    @NotNull
    private Long productoId;

    @NotNull
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;

    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
