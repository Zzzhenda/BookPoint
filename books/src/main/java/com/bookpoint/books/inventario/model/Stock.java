package com.bookpoint.books.inventario.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad Stock: cantidad de un producto disponible en una sucursal.
 * Las referencias a producto y sucursal son por id (referencia logica
 * dentro del mismo monolito modular).
 */
@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productoId;

    @Column(nullable = false)
    private Long sucursalId;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Integer stockMinimo;
}
