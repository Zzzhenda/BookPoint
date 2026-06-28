package com.bookpoint.pedidos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Datos para generar un pedido: de que cliente y a partir de que carrito.
 */
@Data
public class PedidoRequestDTO {

    @NotNull(message = "El id del cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El id del carrito es obligatorio")
    private Long carritoId;
}
