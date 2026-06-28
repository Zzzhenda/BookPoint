package com.bookpoint.carrito.repository;

import com.bookpoint.carrito.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de Carrito.
 */
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    /** Carritos de un cliente. */
    List<Carrito> findByClienteId(Long clienteId);
}
