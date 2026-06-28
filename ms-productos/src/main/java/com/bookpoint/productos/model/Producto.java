package com.bookpoint.productos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidad Producto: un item del catalogo (libro o util).
 *
 * No guarda stock: el stock por sucursal vive en ms-inventario.
 * Asi cada microservicio tiene una sola responsabilidad.
 */
@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Titulo del libro o nombre del util. */
    @Column(nullable = false, length = 150)
    private String titulo;

    /** Autor (solo aplica a libros; en utiles puede quedar vacio). */
    @Column(length = 120)
    private String autor;

    @Column(length = 120)
    private String editorial;

    @Column(length = 80)
    private String genero;

    /** LIBRO o UTIL. Se guarda como texto en la BD (no como numero). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoProducto tipo;

    /** Precio en pesos. BigDecimal evita errores de redondeo con dinero. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(length = 20)
    private String isbn;

    @Column(nullable = false)
    private Boolean activo = true;
}
