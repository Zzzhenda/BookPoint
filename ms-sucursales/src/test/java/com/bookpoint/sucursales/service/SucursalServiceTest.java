package com.bookpoint.sucursales.service;

import com.bookpoint.sucursales.TestDataFactory;
import com.bookpoint.sucursales.dto.SucursalRequestDTO;
import com.bookpoint.sucursales.dto.SucursalResponseDTO;
import com.bookpoint.sucursales.exception.RecursoNoEncontradoException;
import com.bookpoint.sucursales.model.Sucursal;
import com.bookpoint.sucursales.repository.SucursalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de SucursalService.
 *
 * Con Mockito simulamos el repositorio (@Mock) para no tocar la base de datos
 * real, y probamos solo la logica de negocio del service (@InjectMocks).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SucursalService - Pruebas Unitarias")
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalService sucursalService;

    @Test
    @DisplayName("crear: guarda la sucursal cuando los datos son validos")
    void crear_datosValidos_guardaSucursal() {
        SucursalRequestDTO request = TestDataFactory.unRequestValido();
        Sucursal guardada = TestDataFactory.unaSucursal();

        when(sucursalRepository.existsByNombre(request.getNombre())).thenReturn(false);
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(guardada);

        SucursalResponseDTO resultado = sucursalService.crear(request);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(guardada.getId());
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    @DisplayName("crear: lanza excepcion si el nombre ya existe")
    void crear_nombreDuplicado_lanzaExcepcion() {
        SucursalRequestDTO request = TestDataFactory.unRequestValido();
        when(sucursalRepository.existsByNombre(request.getNombre())).thenReturn(true);

        assertThatThrownBy(() -> sucursalService.crear(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ya existe una sucursal");

        verify(sucursalRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear: lanza excepcion si la ciudad no es una de las permitidas")
    void crear_ciudadInvalida_lanzaExcepcion() {
        SucursalRequestDTO request = TestDataFactory.unRequestValido();
        request.setCiudad("Santiago"); // BookPoint no opera aqui

        assertThatThrownBy(() -> sucursalService.crear(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ciudad no valida");

        verify(sucursalRepository, never()).save(any());
    }

    @Test
    @DisplayName("obtenerPorId: lanza 404 cuando la sucursal no existe")
    void obtenerPorId_noExiste_lanzaRecursoNoEncontrado() {
        when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sucursalService.obtenerPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("No existe la sucursal");
    }
}
