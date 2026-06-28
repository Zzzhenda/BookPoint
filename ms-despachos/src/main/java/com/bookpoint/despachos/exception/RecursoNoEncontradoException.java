package com.bookpoint.despachos.exception;

/**
 * Se lanza cuando se busca un despacho que no existe.
 * El GlobalExceptionHandler la traduce a un 404 Not Found.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
