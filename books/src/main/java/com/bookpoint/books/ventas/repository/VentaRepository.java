package com.bookpoint.books.ventas.repository;

import com.bookpoint.books.ventas.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}
