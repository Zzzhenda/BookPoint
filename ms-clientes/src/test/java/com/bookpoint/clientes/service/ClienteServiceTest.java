package com.bookpoint.clientes.service;

import com.bookpoint.clientes.TestDataFactory;
import com.bookpoint.clientes.dto.ClienteRequestDTO;
import com.bookpoint.clientes.dto.ClienteResponseDTO;
import com.bookpoint.clientes.dto.DireccionRequestDTO;
import com.bookpoint.clientes.dto.DireccionResponseDTO;
import com.bookpoint.clientes.exception.RecursoNoEncontradoException;
import com.bookpoint.clientes.model.Cliente;
import com.bookpoint.clientes.repository.ClienteRepository;
import com.bookpoint.clientes.repository.DireccionRepository;
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
 * Pruebas unitarias de ClienteService.
 * Simulamos los repositorios con Mockito; no se toca la base de datos real.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteService - Pruebas Unitarias")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private DireccionRepository direccionRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    @DisplayName("crear: guarda el cliente cuando el email no existe")
    void crear_emailNuevo_guardaCliente() {
        ClienteRequestDTO request = TestDataFactory.unRequestValido();
        Cliente guardado = TestDataFactory.unCliente();

        when(clienteRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(guardado);

        ClienteResponseDTO resultado = clienteService.crear(request);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(guardado.getId());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    @DisplayName("crear: lanza excepcion si el email ya esta registrado")
    void crear_emailDuplicado_lanzaExcepcion() {
        ClienteRequestDTO request = TestDataFactory.unRequestValido();
        when(clienteRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> clienteService.crear(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ya existe un cliente con el email");

        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("obtenerPorId: lanza 404 cuando el cliente no existe")
    void obtenerPorId_noExiste_lanzaRecursoNoEncontrado() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.obtenerPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("No existe el cliente");
    }

    @Test
    @DisplayName("agregarDireccion: enlaza la direccion al cliente y la devuelve")
    void agregarDireccion_clienteExiste_agregaDireccion() {
        Cliente cliente = TestDataFactory.unCliente();
        when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        DireccionRequestDTO dto = new DireccionRequestDTO();
        dto.setCalle("Barros Arana");
        dto.setNumero("1234");
        dto.setComuna("Concepcion");
        dto.setCiudad("Concepcion");
        dto.setRegion("Biobio");

        DireccionResponseDTO resultado = clienteService.agregarDireccion(cliente.getId(), dto);

        assertThat(resultado.getCalle()).isEqualTo("Barros Arana");
        assertThat(cliente.getDirecciones()).hasSize(1);
        verify(clienteRepository).save(cliente);
    }
}
