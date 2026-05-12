package com.bookpoint.books.sucursales.repository;

import com.bookpoint.books.sucursales.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
}
