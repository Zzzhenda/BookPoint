package com.bookpoint.productos.repository;

import com.bookpoint.productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio de Producto. Ademas del CRUD de JpaRepository, define los
 * filtros del catalogo que pide el caso (autor, genero, precio).
 * Spring Data implementa estos metodos a partir de su nombre.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByAutorIgnoreCase(String autor);

    List<Producto> findByGeneroIgnoreCase(String genero);

    /** Productos con precio menor o igual al indicado. */
    List<Producto> findByPrecioLessThanEqual(BigDecimal precio);
}
