package com.bookpoint.carrito.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Carrito: el carrito de compra de un cliente.
 * Un carrito tiene varios items (relacion uno-a-muchos).
 *
 * Usamos @Getter/@Setter (no @Data) por la relacion bidireccional con ItemCarrito.
 */
@Entity
@Table(name = "carritos")
@Getter
@Setter
@NoArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Id del cliente dueno del carrito (referencia a ms-clientes). */
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrito> items = new ArrayList<>();

    public void agregarItem(ItemCarrito item) {
        item.setCarrito(this);
        this.items.add(item);
    }

    public void quitarItem(ItemCarrito item) {
        this.items.remove(item);
        item.setCarrito(null);
    }
}
