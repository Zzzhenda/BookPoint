package com.bookpoint.books.exception;

/**
 * Excepcion personalizada que se lanza cuando un recurso solicitado
 * (producto, sucursal, cliente, etc.) no existe en la base de datos.
 * El GlobalExceptionHandler la traduce a un HTTP 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
