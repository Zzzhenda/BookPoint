package com.bookpoint.carrito.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Datos del carrito que devolvemos al exterior, con sus items y el total.
 */
@Data
public class CarritoResponseDTO {

    private Long id;
    private Long clienteId;
    private LocalDateTime fechaCreacion;
    private List<ItemResponseDTO> items;
    private BigDecimal total;
}
