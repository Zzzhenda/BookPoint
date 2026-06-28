package com.bookpoint.carrito.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Representa los datos del producto que nos interesan de ms-productos.
 * Solo incluimos los campos que necesita el carrito (id, titulo, precio);
 * los demas campos del JSON remoto se ignoran.
 */
@Data
public class ProductoDTO {

    private Long id;
    private String titulo;
    private BigDecimal precio;
}
