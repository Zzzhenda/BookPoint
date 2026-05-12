package com.bookpoint.books.proveedores.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad Proveedor: empresa editorial o distribuidora que abastece a BookPoint.
 */
@Entity
@Table(name = "proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(unique = true, length = 20)
    private String rut;

    @Column(length = 100)
    private String contactoNombre;

    @Column(length = 100)
    private String contactoEmail;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false)
    private Boolean activo;
}
