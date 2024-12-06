package com.bank.CustomerMs.service;

import com.bank.CustomerMs.client.CuentaClient;
import com.bank.CustomerMs.exception.ClienteException;
import com.bank.CustomerMs.model.Cliente;
import com.bank.CustomerMs.repository.ClienteRepository;
import com.bank.CustomerMs.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CuentaClient cuentaClient;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .id(1)
                .nombre("Juan")
                .apellido("Pérez")
                .dni("12345678")
                .email("juan@example.com")
                .build();
    }

    @Test
    void crearCliente_DatosValidos_RetornaClienteCreado() {
        when(clienteRepository.existsByDni(any())).thenReturn(false);
        when(clienteRepository.save(any())).thenReturn(cliente);

        Cliente resultado = clienteService.crearCliente(cliente);

        assertNotNull(resultado);
        assertEquals(cliente.getDni(), resultado.getDni());
        verify(clienteRepository).save(any());
    }

    @Test
    void crearCliente_DniDuplicado_LanzaExcepcion() {
        when(clienteRepository.existsByDni(any())).thenReturn(true);

        assertThrows(ClienteException.class, () -> clienteService.crearCliente(cliente));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void crearCliente_DniInvalido_LanzaExcepcion() {
        cliente.setDni("123"); // DNI inválido

        assertThrows(ClienteException.class, () -> clienteService.crearCliente(cliente));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void obtenerClientePorId_ClienteExiste_RetornaCliente() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.obtenerClientePorId(1);

        assertNotNull(resultado);
        assertEquals(cliente.getId(), resultado.getId());
    }

    @Test
    void obtenerClientePorId_ClienteNoExiste_LanzaExcepcion() {
        when(clienteRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ClienteException.class, () -> clienteService.obtenerClientePorId(1));
    }

    @Test
    void listarClientes_ExistenClientes_RetornaLista() {
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> resultado = clienteService.listarClientes();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void actualizarCliente_DatosValidos_RetornaClienteActualizado() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any())).thenReturn(cliente);

        Cliente clienteActualizado = cliente;
        clienteActualizado.setNombre("Juan Actualizado");

        Cliente resultado = clienteService.actualizarCliente(1, clienteActualizado);

        assertNotNull(resultado);
        assertEquals("Juan Actualizado", resultado.getNombre());
    }

    @Test
    void eliminarCliente_ClienteSinCuentas_EliminaCliente() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(cuentaClient.tieneCuentasActivas(1)).thenReturn(false);

        clienteService.eliminarCliente(1);

        verify(clienteRepository).deleteById(1);
    }

    @Test
    void eliminarCliente_ClienteConCuentas_LanzaExcepcion() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(cuentaClient.tieneCuentasActivas(1)).thenReturn(true);

        assertThrows(ClienteException.class, () -> clienteService.eliminarCliente(1));
        verify(clienteRepository, never()).deleteById(any());
    }
}