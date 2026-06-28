package com.bookpoint.carrito.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Datos de una linea del carrito que devolvemos al exterior.
 */
@Data
public class ItemResponseDTO {

    private Long id;
    private Long productoId;
    private String tituloProducto;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;
}
