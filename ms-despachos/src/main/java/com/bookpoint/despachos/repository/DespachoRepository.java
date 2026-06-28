package com.bookpoint.despachos.repository;

import com.bookpoint.despachos.model.Despacho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de Despacho.
 */
public interface DespachoRepository extends JpaRepository<Despacho, Long> {

    /** Para no crear dos despachos para el mismo pedido. */
    boolean existsByPedidoId(Long pedidoId);

    /** Despacho asociado a un pedido. */
    Optional<Despacho> findByPedidoId(Long pedidoId);
}
