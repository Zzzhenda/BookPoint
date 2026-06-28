package com.bookpoint.pedidos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * El carrito completo tal como llega desde ms-carrito, con sus items y total.
 */
@Data
public class CarritoDTO {

    private Long id;
    private Long clienteId;
    private List<ItemCarritoDTO> items;
    private BigDecimal total;
}
