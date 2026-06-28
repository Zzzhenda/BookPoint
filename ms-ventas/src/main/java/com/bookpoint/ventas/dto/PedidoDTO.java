package com.bookpoint.ventas.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Datos del pedido que nos interesan de ms-pedidos: para validar que existe
 * y tomar su total.
 */
@Data
public class PedidoDTO {

    private Long id;
    private Long clienteId;
    private String estado;
    private BigDecimal total;
}
