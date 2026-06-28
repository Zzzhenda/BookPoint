package com.bookpoint.despachos.dto;

import lombok.Data;

/**
 * Datos del pedido que nos interesan de ms-pedidos (para validar que existe).
 */
@Data
public class PedidoDTO {

    private Long id;
    private Long clienteId;
    private String estado;
}
