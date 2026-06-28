package com.bookpoint.productos.dto;

import com.bookpoint.productos.model.TipoProducto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Datos que llegan para crear o actualizar un producto del catalogo.
 */
@Data
public class ProductoRequestDTO {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 150, message = "El titulo no puede superar los 150 caracteres")
    private String titulo;

    @Size(max = 120, message = "El autor no puede superar los 120 caracteres")
    private String autor;

    @Size(max = 120, message = "La editorial no puede superar los 120 caracteres")
    private String editorial;

    @Size(max = 80, message = "El genero no puede superar los 80 caracteres")
    private String genero;

    @NotNull(message = "El tipo es obligatorio (LIBRO o UTIL)")
    private TipoProducto tipo;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private BigDecimal precio;

    @Size(max = 20, message = "El ISBN no puede superar los 20 caracteres")
    private String isbn;
}
