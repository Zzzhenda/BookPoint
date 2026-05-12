package com.bookpoint.books.inventario.repository;

import com.bookpoint.books.inventario.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductoIdAndSucursalId(Long productoId, Long sucursalId);
    List<Stock> findBySucursalId(Long sucursalId);
}
