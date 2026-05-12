package com.bookpoint.books.proveedores.service;

import com.bookpoint.books.exception.RecursoNoEncontradoException;
import com.bookpoint.books.exception.ReglaNegocioException;
import com.bookpoint.books.proveedores.dto.ProveedorDTO;
import com.bookpoint.books.proveedores.model.Proveedor;
import com.bookpoint.books.proveedores.repository.ProveedorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {

    private static final Logger log = LoggerFactory.getLogger(ProveedorService.class);

    private final ProveedorRepository repository;

    public ProveedorService(ProveedorRepository repository) {
        this.repository = repository;
    }

    public List<ProveedorDTO> listar() {
        log.info("Listando proveedores");
        return repository.findAll().stream().map(this::aDTO).toList();
    }

    public ProveedorDTO buscarPorId(Long id) {
        log.info("Buscando proveedor id {}", id);
        return aDTO(repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Proveedor con id " + id + " no encontrado")));
    }

    public ProveedorDTO crear(ProveedorDTO dto) {
        log.info("Creando proveedor: {}", dto.getNombre());
        // Regla de negocio: si trae rut, debe ser unico.
        if (dto.getRut() != null && !dto.getRut().isBlank()
                && repository.findByRut(dto.getRut()).isPresent()) {
            throw new ReglaNegocioException("Ya existe un proveedor con el rut " + dto.getRut());
        }
        Proveedor p = aEntidad(dto);
        if (p.getActivo() == null) p.setActivo(true);
        return aDTO(repository.save(p));
    }

    public ProveedorDTO actualizar(Long id, ProveedorDTO dto) {
        log.info("Actualizando proveedor id {}", id);
        Proveedor p = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Proveedor con id " + id + " no encontrado"));
        p.setNombre(dto.getNombre());
        p.setRut(dto.getRut());
        p.setContactoNombre(dto.getContactoNombre());
        p.setContactoEmail(dto.getContactoEmail());
        p.setTelefono(dto.getTelefono());
        if (dto.getActivo() != null) p.setActivo(dto.getActivo());
        return aDTO(repository.save(p));
    }

    public void eliminar(Long id) {
        log.info("Eliminando proveedor id {}", id);
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Proveedor con id " + id + " no encontrado");
        }
        repository.deleteById(id);
    }

    private ProveedorDTO aDTO(Proveedor p) {
        return ProveedorDTO.builder().id(p.getId()).nombre(p.getNombre()).rut(p.getRut())
                .contactoNombre(p.getContactoNombre()).contactoEmail(p.getContactoEmail())
                .telefono(p.getTelefono()).activo(p.getActivo()).build();
    }

    private Proveedor aEntidad(ProveedorDTO d) {
        return Proveedor.builder().id(d.getId()).nombre(d.getNombre()).rut(d.getRut())
                .contactoNombre(d.getContactoNombre()).contactoEmail(d.getContactoEmail())
                .telefono(d.getTelefono()).activo(d.getActivo()).build();
    }
}
