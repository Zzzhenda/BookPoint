package com.bookpoint.inventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Inventario: el stock de UN producto en UNA sucursal.
 *
 * productoId y sucursalId son referencias a otros microservicios
 * (ms-productos y ms-sucursales). No son claves foraneas reales porque
 * cada microservicio tiene su propia base de datos; solo guardamos el id.
 *
 * La restriccion unica (producto_id, sucursal_id) evita que existan dos
 * registros de stock del mismo producto en la misma sucursal.
 */
@Entity
@Table(name = "inventario",
        uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "sucursal_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "sucursal_id", nullable = false)
    private Long sucursalId;

    /** Unidades disponibles en esa sucursal. */
    @Column(nullable = false)
    private Integer cantidad;

    /** Umbral minimo: si la cantidad baja de aqui, hay que reponer. */
    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo;
}
