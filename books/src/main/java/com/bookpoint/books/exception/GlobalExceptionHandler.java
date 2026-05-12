package com.bookpoint.books.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador centralizado de excepciones para toda la API.
 * Convierte las excepciones lanzadas por los services en respuestas
 * JSON consistentes, con el codigo HTTP que corresponde.
 *
 * Cubre el indicador IE 2.3.1 de la rubrica (manejo de excepciones con
 * @ControllerAdvice y respuestas controladas).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** 404 cuando un recurso no existe. */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return construir(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** 409 cuando una regla de negocio falla (stock insuficiente, duplicado, etc.). */
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, Object>> manejarReglaNegocio(ReglaNegocioException ex) {
        log.warn("Regla de negocio violada: {}", ex.getMessage());
        return construir(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** 400 cuando un DTO no pasa las validaciones de Bean Validation. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errores.put(err.getField(), err.getDefaultMessage()));
        log.warn("Validacion fallida: {}", errores);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("estado", HttpStatus.BAD_REQUEST.value());
        respuesta.put("mensaje", "Errores de validacion");
        respuesta.put("errores", errores);
        return ResponseEntity.badRequest().body(respuesta);
    }

    /** 500 para cualquier excepcion no controlada. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarGenerica(Exception ex) {
        log.error("Error no controlado: ", ex);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor: " + ex.getMessage());
    }

    /** Construye el cuerpo JSON con la misma estructura para todos los errores. */
    private ResponseEntity<Map<String, Object>> construir(HttpStatus estado, String mensaje) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("estado", estado.value());
        cuerpo.put("mensaje", mensaje);
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
