package com.bookpoint.books.pedidos.model;

/** Estados por los que pasa un pedido en su ciclo de vida. */
public enum EstadoPedido {
    PENDIENTE,
    CONFIRMADO,
    PREPARADO,
    DESPACHADO,
    ENTREGADO,
    CANCELADO
}
