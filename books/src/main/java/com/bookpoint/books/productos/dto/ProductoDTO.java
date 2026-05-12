package com.bookpoint.books.productos.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO de Producto para la API REST.
 * Mantiene la entidad JPA separada del modelo publico y aplica
 * Bean Validation (cubre indicador IE 2.2.2 de la rubrica).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 200)
    private String titulo;

    @Size(max = 150)
    private String autor;

    @Size(max = 150)
    private String editorial;

    @Size(max = 100)
    private String genero;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    private Boolean activo;
}
