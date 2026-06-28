package com.bookpoint.sucursales.service;

import com.bookpoint.sucursales.dto.SucursalRequestDTO;
import com.bookpoint.sucursales.dto.SucursalResponseDTO;
import com.bookpoint.sucursales.exception.RecursoNoEncontradoException;
import com.bookpoint.sucursales.model.Sucursal;
import com.bookpoint.sucursales.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Logica de negocio de las sucursales.
 *
 * El controller solo recibe peticiones y delega aqui; este service es el que
 * aplica las reglas de negocio, habla con el repositorio y arma las respuestas.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SucursalService {

    private final SucursalRepository sucursalRepository;

    /**
     * Ciudades en las que BookPoint Chile tiene operacion.
     * Si llega cualquier otra ciudad, la rechazamos como regla de negocio.
     */
    private static final Set<String> CIUDADES_PERMITIDAS =
            Set.of("Concepcion", "Temuco", "La Serena");

    // ------------------------- Lecturas (GET) -------------------------

    /** Devuelve todas las sucursales registradas. */
    public List<SucursalResponseDTO> listarTodas() {
        return sucursalRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Busca una sucursal por id; si no existe lanza 404. */
    public SucursalResponseDTO obtenerPorId(Long id) {
        Sucursal sucursal = buscarSucursalOFallar(id);
        return convertirAResponse(sucursal);
    }

    /** Devuelve las sucursales de una ciudad. */
    public List<SucursalResponseDTO> listarPorCiudad(String ciudad) {
        return sucursalRepository.findByCiudadIgnoreCase(ciudad).stream()
                .map(this::convertirAResponse)
                .toList();
    }

    // ------------------------- Escrituras (POST/PUT/DELETE) -------------------------

    /** Crea una nueva sucursal aplicando las reglas de negocio. */
    @Transactional
    public SucursalResponseDTO crear(SucursalRequestDTO dto) {
        validarCiudadPermitida(dto.getCiudad());

        if (sucursalRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe una sucursal con el nombre: " + dto.getNombre());
        }

        Sucursal sucursal = new Sucursal();
        copiarDatos(dto, sucursal);
        sucursal.setActiva(true);

        Sucursal guardada = sucursalRepository.save(sucursal);
        log.info("Sucursal creada: '{}' en {} (id={})",
                guardada.getNombre(), guardada.getCiudad(), guardada.getId());
        return convertirAResponse(guardada);
    }

    /** Actualiza los datos de una sucursal existente. */
    @Transactional
    public SucursalResponseDTO actualizar(Long id, SucursalRequestDTO dto) {
        Sucursal sucursal = buscarSucursalOFallar(id);
        validarCiudadPermitida(dto.getCiudad());

        // Solo controlamos el nombre duplicado si el nombre realmente cambio
        boolean cambioNombre = !sucursal.getNombre().equals(dto.getNombre());
        if (cambioNombre && sucursalRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe una sucursal con el nombre: " + dto.getNombre());
        }

        copiarDatos(dto, sucursal);
        Sucursal actualizada = sucursalRepository.save(sucursal);
        log.info("Sucursal actualizada: id={}", actualizada.getId());
        return convertirAResponse(actualizada);
    }

    /** Elimina una sucursal por id; si no existe lanza 404. */
    @Transactional
    public void eliminar(Long id) {
        Sucursal sucursal = buscarSucursalOFallar(id);
        sucursalRepository.delete(sucursal);
        log.info("Sucursal eliminada: id={}", id);
    }

    // ------------------------- Metodos privados de apoyo -------------------------

    /** Busca la sucursal o lanza la excepcion de 404 si no aparece. */
    private Sucursal buscarSucursalOFallar(Long id) {
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la sucursal con id " + id));
    }

    /** Regla de negocio: la ciudad debe ser una de las 3 donde opera BookPoint. */
    private void validarCiudadPermitida(String ciudad) {
        if (!CIUDADES_PERMITIDAS.contains(ciudad)) {
            throw new RuntimeException(
                    "Ciudad no valida. BookPoint solo opera en: " + CIUDADES_PERMITIDAS);
        }
    }

    /** Vuelca los datos del DTO de entrada sobre la entidad. */
    private void copiarDatos(SucursalRequestDTO dto, Sucursal sucursal) {
        sucursal.setNombre(dto.getNombre());
        sucursal.setCiudad(dto.getCiudad());
        sucursal.setDireccion(dto.getDireccion());
        sucursal.setTelefono(dto.getTelefono());
        sucursal.setHorario(dto.getHorario());
    }

    /** Convierte la entidad en el DTO de salida que viaja al cliente. */
    private SucursalResponseDTO convertirAResponse(Sucursal sucursal) {
        SucursalResponseDTO dto = new SucursalResponseDTO();
        dto.setId(sucursal.getId());
        dto.setNombre(sucursal.getNombre());
        dto.setCiudad(sucursal.getCiudad());
        dto.setDireccion(sucursal.getDireccion());
        dto.setTelefono(sucursal.getTelefono());
        dto.setHorario(sucursal.getHorario());
        dto.setActiva(sucursal.getActiva());
        return dto;
    }
}
