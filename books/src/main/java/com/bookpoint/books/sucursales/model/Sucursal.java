package com.bookpoint.books.sucursales.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad Sucursal: una tienda fisica de BookPoint
 * (Concepcion, Temuco o La Serena segun el caso).
 */
@Entity
@Table(name = "sucursales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(length = 200)
    private String direccion;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String horario;

    @Column(nullable = false)
    private Boolean activa;
}
