package com.bookpoint.books.bodega.repository;

import com.bookpoint.books.bodega.model.StockCentral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockCentralRepository extends JpaRepository<StockCentral, Long> {
    Optional<StockCentral> findByProductoId(Long productoId);
}
