package com.bookpoint.clientes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad Direccion: una direccion de despacho/contacto de un cliente.
 *
 * Muchas direcciones pertenecen a UN cliente (relacion muchos-a-uno).
 * La columna cliente_id es la clave foranea hacia la tabla clientes.
 */
@Entity
@Table(name = "direcciones")
@Getter
@Setter
@NoArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String calle;

    @Column(length = 10)
    private String numero;

    @Column(nullable = false, length = 80)
    private String comuna;

    @Column(nullable = false, length = 80)
    private String ciudad;

    @Column(nullable = false, length = 80)
    private String region;

    /**
     * Cliente dueno de la direccion.
     * @JoinColumn define la clave foranea cliente_id en la tabla direcciones.
     */
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}
