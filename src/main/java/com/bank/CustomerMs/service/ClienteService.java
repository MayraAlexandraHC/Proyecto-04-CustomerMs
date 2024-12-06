package com.bank.CustomerMs.service;

import com.bank.CustomerMs.model.Cliente;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClienteService {
    Cliente crearCliente(Cliente cliente);
    List<Cliente> listarClientes();
    Cliente obtenerClientePorId(Integer id);
    Cliente actualizarCliente(Integer id, Cliente cliente);
    void eliminarCliente(Integer id);
}

