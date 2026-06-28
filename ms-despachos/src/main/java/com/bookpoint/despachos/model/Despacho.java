package com.bookpoint.despachos.model;

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

import java.time.LocalDateTime;

/**
 * Entidad Despacho: el envio de un pedido.
 *
 * pedidoId referencia al pedido en ms-pedidos y es unico: un pedido tiene un
 * solo despacho.
 */
@Entity
@Table(name = "despachos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false, unique = true)
    private Long pedidoId;

    @Column(name = "direccion_envio", nullable = false, length = 200)
    private String direccionEnvio;

    /** Sucursal desde la que sale el envio (referencia a ms-sucursales). */
    @Column(name = "sucursal_origen_id", nullable = false)
    private Long sucursalOrigenId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private EstadoDespacho estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    /** Se llena cuando el estado pasa a ENTREGADO. */
    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;
}
