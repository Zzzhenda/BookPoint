package com.bookpoint.clientes.repository;

import com.bookpoint.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de Cliente. JpaRepository ya entrega el CRUD completo.
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /** Para validar que el email no se repita. */
    boolean existsByEmail(String email);

    /** Buscar por email (util si otro microservicio quiere validar por correo). */
    Optional<Cliente> findByEmail(String email);
}
