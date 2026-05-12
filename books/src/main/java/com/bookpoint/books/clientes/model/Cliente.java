package com.bookpoint.books.clientes.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Cliente del canal web. Un cliente puede tener varias direcciones
 * de despacho (relacion @OneToMany, cubre IE 2.2.3 de la rubrica).
 */
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Direccion> direcciones = new ArrayList<>();
}
