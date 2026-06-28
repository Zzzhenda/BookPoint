package com.bookpoint.pedidos.repository;

import com.bookpoint.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de Pedido.
 */
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /** Pedidos de un cliente (historial). */
    List<Pedido> findByClienteId(Long clienteId);
}
