package com.bookpoint.sucursales.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador central de errores del microservicio.
 *
 * Con @RestControllerAdvice atrapamos las excepciones de TODOS los endpoints
 * en un solo lugar y devolvemos una respuesta JSON coherente, evitando repetir
 * try/catch en cada controlador.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Recurso inexistente -> 404 Not Found. */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    /** Reglas de negocio incumplidas (ej: nombre duplicado, ciudad invalida) -> 400 Bad Request. */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> manejarReglaNegocio(RuntimeException ex) {
        log.warn("Solicitud invalida: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }

    /** Fallos de validacion de Bean Validation -> 400 con el detalle por campo. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage()));
        log.warn("Errores de validacion: {}", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }
}
