package com.bookpoint.books.usuarios.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad Usuario interno del sistema BookPoint.
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Rol rol;

    /** Id de la sucursal donde trabaja el usuario (referencia logica). */
    private Long sucursalId;

    @Column(nullable = false)
    private Boolean activo;
}
