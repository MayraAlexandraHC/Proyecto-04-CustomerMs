package com.bank.CustomerMs.repository;

import com.bank.CustomerMs.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByDni(String dni);
    boolean existsByDni(String dni);
    Optional<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);
}

