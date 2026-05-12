package com.bookpoint.books.productos.repository;

import com.bookpoint.books.productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByAutorContainingIgnoreCase(String autor);
    List<Producto> findByEditorialContainingIgnoreCase(String editorial);
    List<Producto> findByGeneroContainingIgnoreCase(String genero);
}
