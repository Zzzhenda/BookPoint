package com.bookpoint.ventas.exception;

/**
 * Se lanza cuando se busca una venta que no existe.
 * El GlobalExceptionHandler la traduce a un 404 Not Found.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
