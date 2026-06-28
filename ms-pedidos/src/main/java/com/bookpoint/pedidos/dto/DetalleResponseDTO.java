package com.bookpoint.pedidos.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Una linea del pedido que devolvemos al exterior.
 */
@Data
public class DetalleResponseDTO {

    private Long id;
    private Long productoId;
    private String tituloProducto;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;
}
