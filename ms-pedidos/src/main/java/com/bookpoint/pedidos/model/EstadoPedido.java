package com.bookpoint.pedidos.model;

/**
 * Estados por los que pasa un pedido.
 * CREADO: recien generado. PAGADO: ms-ventas registro la boleta.
 * DESPACHADO: ms-despachos lo envio. ANULADO: cancelado.
 */
public enum EstadoPedido {
    CREADO,
    PAGADO,
    DESPACHADO,
    ANULADO
}
