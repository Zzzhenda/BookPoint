package com.bookpoint.books.bodega.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad StockCentral: cantidad de un producto en la bodega central.
 * No esta separada por sucursal (eso es Stock en el modulo inventario).
 */
@Entity
@Table(name = "stock_central")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockCentral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long productoId;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Integer stockMinimo;

    @Column(length = 50)
    private String ubicacion;
}
