package com.bookpoint.despachos.model;

/**
 * Estados por los que pasa un despacho.
 * PENDIENTE: recien creado. EN_RUTA: salio a reparto.
 * ENTREGADO: llego al cliente. CANCELADO: anulado.
 */
public enum EstadoDespacho {
    PENDIENTE,
    EN_RUTA,
    ENTREGADO,
    CANCELADO
}
