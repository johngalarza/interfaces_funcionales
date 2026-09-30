package com.interfaces.funcionales.tarea.repository;

import com.interfaces.funcionales.tarea.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}