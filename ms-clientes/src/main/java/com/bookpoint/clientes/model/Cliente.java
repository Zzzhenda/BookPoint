package com.bookpoint.clientes.model;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Cliente: representa a una persona registrada en BookPoint Chile.
 *
 * Un cliente puede tener VARIAS direcciones (relacion uno-a-muchos).
 *
 * Nota: usamos @Getter/@Setter en vez de @Data porque la relacion es
 * bidireccional (Cliente <-> Direccion). Con @Data, los toString()/equals()
 * generados se llamarian en circulo entre ambas clases y provocarian un error.
 */
@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellido;

    /** Correo del cliente. Es unico: no puede haber dos clientes con el mismo email. */
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false)
    private Boolean activo = true;

    /**
     * Direcciones del cliente.
     * - mappedBy = "cliente": el dueno de la relacion es la columna cliente_id de Direccion.
     * - cascade = ALL + orphanRemoval: al guardar/borrar el cliente, sus direcciones lo siguen.
     */
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direccion> direcciones = new ArrayList<>();

    /** Agrega una direccion manteniendo sincronizados ambos lados de la relacion. */
    public void agregarDireccion(Direccion direccion) {
        direccion.setCliente(this);
        this.direcciones.add(direccion);
    }

    /** Quita una direccion manteniendo sincronizados ambos lados de la relacion. */
    public void quitarDireccion(Direccion direccion) {
        this.direcciones.remove(direccion);
        direccion.setCliente(null);
    }
}
