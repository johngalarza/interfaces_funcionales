package com.interfaces.funcionales.tarea.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interfaces.funcionales.tarea.model.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
}
