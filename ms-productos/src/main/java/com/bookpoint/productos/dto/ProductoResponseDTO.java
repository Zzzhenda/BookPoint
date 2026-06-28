package com.bookpoint.productos.dto;

import com.bookpoint.productos.model.TipoProducto;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Datos de un producto que devolvemos al exterior.
 */
@Data
public class ProductoResponseDTO {

    private Long id;
    private String titulo;
    private String autor;
    private String editorial;
    private String genero;
    private TipoProducto tipo;
    private BigDecimal precio;
    private String isbn;
    private Boolean activo;
}
