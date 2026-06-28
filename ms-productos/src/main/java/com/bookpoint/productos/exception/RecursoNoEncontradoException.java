package com.bookpoint.productos.exception;

/**
 * Se lanza cuando se busca un producto que no existe.
 * El GlobalExceptionHandler la traduce a un 404 Not Found.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
