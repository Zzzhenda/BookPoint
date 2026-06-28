package com.bookpoint.pedidos.dto;

import com.bookpoint.pedidos.model.EstadoPedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Datos del pedido que devolvemos al exterior, con sus detalles y total.
 * ms-ventas y ms-despachos consumen este formato.
 */
@Data
public class PedidoResponseDTO {

    private Long id;
    private Long clienteId;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private BigDecimal total;
    private List<DetalleResponseDTO> detalles;
}
