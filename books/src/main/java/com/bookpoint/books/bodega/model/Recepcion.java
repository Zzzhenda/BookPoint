package com.bookpoint.books.bodega.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Recepcion: ingreso de mercaderia desde un proveedor a la bodega.
 * Una recepcion contiene varios detalles (uno por producto).
 * Relacion @OneToMany con DetalleRecepcion.
 */
@Entity
@Table(name = "recepciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recepcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long proveedorId;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 200)
    private String observaciones;

    @OneToMany(mappedBy = "recepcion", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<DetalleRecepcion> detalles = new ArrayList<>();
}
