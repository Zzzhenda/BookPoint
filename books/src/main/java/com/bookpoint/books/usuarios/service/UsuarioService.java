package com.bookpoint.books.usuarios.service;

import com.bookpoint.books.exception.RecursoNoEncontradoException;
import com.bookpoint.books.exception.ReglaNegocioException;
import com.bookpoint.books.usuarios.dto.UsuarioDTO;
import com.bookpoint.books.usuarios.model.Usuario;
import com.bookpoint.books.usuarios.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<UsuarioDTO> listar() {
        log.info("Listando usuarios");
        return repository.findAll().stream().map(this::aDTO).toList();
    }

    public UsuarioDTO buscarPorId(Long id) {
        log.info("Buscando usuario id {}", id);
        return aDTO(repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Usuario con id " + id + " no encontrado")));
    }

    public UsuarioDTO crear(UsuarioDTO dto) {
        log.info("Creando usuario: {}", dto.getEmail());
        // Regla de negocio: el email debe ser unico.
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ReglaNegocioException("Ya existe un usuario con el email " + dto.getEmail());
        }
        Usuario u = aEntidad(dto);
        if (u.getActivo() == null) u.setActivo(true);
        return aDTO(repository.save(u));
    }

    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        log.info("Actualizando usuario id {}", id);
        Usuario u = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Usuario con id " + id + " no encontrado"));
        u.setNombre(dto.getNombre());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        u.setRol(dto.getRol());
        u.setSucursalId(dto.getSucursalId());
        if (dto.getActivo() != null) u.setActivo(dto.getActivo());
        return aDTO(repository.save(u));
    }

    public void eliminar(Long id) {
        log.info("Eliminando usuario id {}", id);
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario con id " + id + " no encontrado");
        }
        repository.deleteById(id);
    }

    private UsuarioDTO aDTO(Usuario u) {
        return UsuarioDTO.builder().id(u.getId()).nombre(u.getNombre()).email(u.getEmail())
                .password(u.getPassword()).rol(u.getRol()).sucursalId(u.getSucursalId())
                .activo(u.getActivo()).build();
    }

    private Usuario aEntidad(UsuarioDTO d) {
        return Usuario.builder().id(d.getId()).nombre(d.getNombre()).email(d.getEmail())
                .password(d.getPassword()).rol(d.getRol()).sucursalId(d.getSucursalId())
                .activo(d.getActivo()).build();
    }
}
