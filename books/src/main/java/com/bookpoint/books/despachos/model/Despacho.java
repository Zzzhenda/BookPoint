package com.bookpoint.books.despachos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad Despacho: envio asociado a un pedido desde una sucursal hacia
 * una direccion del cliente.
 */
@Entity
@Table(name = "despachos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pedidoId;

    @Column(nullable = false)
    private Long sucursalOrigenId;

    @Column(nullable = false, length = 200)
    private String direccionDestino;

    @Column(nullable = false, length = 100)
    private String ciudadDestino;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaEntrega;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoDespacho estado;

    @Column(length = 200)
    private String observaciones;
}
