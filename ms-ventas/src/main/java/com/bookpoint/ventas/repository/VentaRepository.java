package com.bookpoint.ventas.repository;

import com.bookpoint.ventas.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de Venta.
 */
public interface VentaRepository extends JpaRepository<Venta, Long> {

    /** Para no facturar dos veces el mismo pedido. */
    boolean existsByPedidoId(Long pedidoId);

    /** Venta asociada a un pedido. */
    Optional<Venta> findByPedidoId(Long pedidoId);
}
