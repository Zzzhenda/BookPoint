package com.bookpoint.sucursales.repository;

import com.bookpoint.sucursales.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de Sucursal.
 *
 * Al extender JpaRepository ya tenemos gratis el CRUD completo
 * (findAll, findById, save, deleteById, etc.) sin escribir SQL.
 *
 * Los dos metodos de abajo los "deriva" Spring Data automaticamente
 * a partir de su nombre: no hay que implementarlos.
 */
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    /** Sirve para validar que no se repita el nombre de la sucursal. */
    boolean existsByNombre(String nombre);

    /** Devuelve las sucursales de una ciudad, sin distinguir mayusculas/minusculas. */
    List<Sucursal> findByCiudadIgnoreCase(String ciudad);
}
