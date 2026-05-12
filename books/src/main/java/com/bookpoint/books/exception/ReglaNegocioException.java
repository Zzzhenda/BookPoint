package com.bookpoint.books.exception;

/**
 * Excepcion para violaciones de reglas de negocio. Por ejemplo:
 * stock insuficiente, email duplicado, transicion de estado invalida.
 * El GlobalExceptionHandler la traduce a un HTTP 409 (Conflict).
 */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
