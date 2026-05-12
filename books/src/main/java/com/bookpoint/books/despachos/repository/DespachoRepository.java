package com.bookpoint.books.despachos.repository;

import com.bookpoint.books.despachos.model.Despacho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DespachoRepository extends JpaRepository<Despacho, Long> {
    Optional<Despacho> findByPedidoId(Long pedidoId);
}
