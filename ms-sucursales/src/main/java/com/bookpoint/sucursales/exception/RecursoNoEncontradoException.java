package com.bookpoint.sucursales.exception;

/**
 * Excepcion propia que lanzamos cuando buscamos una sucursal que no existe.
 *
 * La separamos de un RuntimeException generico para poder responder con
 * el codigo HTTP correcto (404 Not Found) en el GlobalExceptionHandler.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
