package com.bookpoint.books.sucursales.service;

import com.bookpoint.books.exception.RecursoNoEncontradoException;
import com.bookpoint.books.sucursales.dto.SucursalDTO;
import com.bookpoint.books.sucursales.model.Sucursal;
import com.bookpoint.books.sucursales.repository.SucursalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {

    private static final Logger log = LoggerFactory.getLogger(SucursalService.class);

    private final SucursalRepository repository;

    public SucursalService(SucursalRepository repository) {
        this.repository = repository;
    }

    public List<SucursalDTO> listar() {
        log.info("Listando sucursales");
        return repository.findAll().stream().map(this::aDTO).toList();
    }

    public SucursalDTO buscarPorId(Long id) {
        log.info("Buscando sucursal id {}", id);
        return aDTO(repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Sucursal con id " + id + " no encontrada")));
    }

    public SucursalDTO crear(SucursalDTO dto) {
        log.info("Creando sucursal: {}", dto.getNombre());
        Sucursal s = aEntidad(dto);
        if (s.getActiva() == null) s.setActiva(true);
        return aDTO(repository.save(s));
    }

    public SucursalDTO actualizar(Long id, SucursalDTO dto) {
        log.info("Actualizando sucursal id {}", id);
        Sucursal s = repository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Sucursal con id " + id + " no encontrada"));
        s.setNombre(dto.getNombre());
        s.setCiudad(dto.getCiudad());
        s.setDireccion(dto.getDireccion());
        s.setTelefono(dto.getTelefono());
        s.setHorario(dto.getHorario());
        if (dto.getActiva() != null) s.setActiva(dto.getActiva());
        return aDTO(repository.save(s));
    }

    public void eliminar(Long id) {
        log.info("Eliminando sucursal id {}", id);
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Sucursal con id " + id + " no encontrada");
        }
        repository.deleteById(id);
    }

    private SucursalDTO aDTO(Sucursal s) {
        return SucursalDTO.builder().id(s.getId()).nombre(s.getNombre())
                .ciudad(s.getCiudad()).direccion(s.getDireccion())
                .telefono(s.getTelefono()).horario(s.getHorario())
                .activa(s.getActiva()).build();
    }

    private Sucursal aEntidad(SucursalDTO d) {
        return Sucursal.builder().id(d.getId()).nombre(d.getNombre())
                .ciudad(d.getCiudad()).direccion(d.getDireccion())
                .telefono(d.getTelefono()).horario(d.getHorario())
                .activa(d.getActiva()).build();
    }
}
