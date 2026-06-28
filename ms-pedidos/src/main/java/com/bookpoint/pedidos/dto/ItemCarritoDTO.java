package com.bookpoint.pedidos.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Una linea del carrito tal como llega desde ms-carrito.
 */
@Data
public class ItemCarritoDTO {

    private Long productoId;
    private String tituloProducto;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;
}
