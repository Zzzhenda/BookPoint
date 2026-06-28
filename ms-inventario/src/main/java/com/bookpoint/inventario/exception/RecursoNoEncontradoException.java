package com.bookpoint.inventario.exception;

/**
 * Se lanza cuando se busca un registro de inventario que no existe.
 * El GlobalExceptionHandler la traduce a un 404 Not Found.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
