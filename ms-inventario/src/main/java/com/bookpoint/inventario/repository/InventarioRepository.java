package com.bookpoint.inventario.repository;

import com.bookpoint.inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repositorio de Inventario.
 */
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    /** Evita registrar dos veces el mismo producto en la misma sucursal. */
    boolean existsByProductoIdAndSucursalId(Long productoId, Long sucursalId);

    /** Stock de un producto en todas las sucursales. */
    List<Inventario> findByProductoId(Long productoId);

    /** Stock de todos los productos de una sucursal. */
    List<Inventario> findBySucursalId(Long sucursalId);

    /**
     * Registros bajo el minimo (necesitan reposicion).
     * Comparamos dos columnas, asi que usamos una consulta JPQL propia.
     */
    @Query("SELECT i FROM Inventario i WHERE i.cantidad <= i.stockMinimo")
    List<Inventario> findBajoStockMinimo();
}
