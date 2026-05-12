package com.bookpoint.books.proveedores.repository;

import com.bookpoint.books.proveedores.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByRut(String rut);
}
