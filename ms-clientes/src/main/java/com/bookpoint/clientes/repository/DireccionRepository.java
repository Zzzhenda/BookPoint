package com.bookpoint.clientes.repository;

import com.bookpoint.clientes.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de Direccion. Lo usamos para buscar/eliminar una direccion puntual.
 */
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
}
