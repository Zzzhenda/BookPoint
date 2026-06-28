package com.bookpoint.carrito.model;

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

import java.math.BigDecimal;

/**
 * Entidad ItemCarrito: una linea del carrito (un producto y su cantidad).
 *
 * Guardamos una "foto" del titulo y el precio del producto en el momento de
 * agregarlo. Asi, si manana cambia el precio en el catalogo, el carrito
 * mantiene el precio con el que el cliente lo agrego.
 */
@Entity
@Table(name = "items_carrito")
@Getter
@Setter
@NoArgsConstructor
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Id del producto en ms-productos. */
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "titulo_producto", nullable = false, length = 150)
    private String tituloProducto;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private Integer cantidad;

    /** precioUnitario * cantidad, calculado al agregar el item. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;
}
