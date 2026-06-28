package com.bookpoint.sucursales.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Sucursal: representa una tienda fisica de BookPoint Chile.
 *
 * Esta clase se transforma en la tabla "sucursales" de la base de datos.
 * Las anotaciones de Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor)
 * generan automaticamente getters, setters y constructores.
 */
@Entity
@Table(name = "sucursales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    /** Identificador unico, autoincremental, generado por MySQL. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de la sucursal. No se repite (ej: "BookPoint Concepcion Centro"). */
    @Column(nullable = false, unique = true, length = 120)
    private String nombre;

    /** Ciudad donde opera. Solo se permiten Concepcion, Temuco y La Serena. */
    @Column(nullable = false, length = 60)
    private String ciudad;

    /** Direccion fisica de la sucursal. */
    @Column(nullable = false, length = 200)
    private String direccion;

    /** Telefono de contacto (opcional). */
    @Column(length = 20)
    private String telefono;

    /** Horario de atencion en texto, ej: "Lun a Vie 9:00-19:00" (opcional). */
    @Column(length = 100)
    private String horario;

    /** Indica si la sucursal esta operativa. Por defecto nace activa. */
    @Column(nullable = false)
    private Boolean activa = true;
}
