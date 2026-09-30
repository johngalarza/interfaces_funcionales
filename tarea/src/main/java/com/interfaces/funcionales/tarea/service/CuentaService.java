package com.interfaces.funcionales.tarea.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.interfaces.funcionales.tarea.model.Cuenta;
import com.interfaces.funcionales.tarea.repository.CuentaRepository;

@Service
public class CuentaService {

    private static final Supplier<BigDecimal> SALDO_POR_DEFECTO = () -> BigDecimal.ZERO;

    private static final Predicate<Cuenta> CUENTA_VALIDA = cuenta ->
            cuenta != null && cuenta.getSaldo() != null;

    private static final Consumer<Cuenta> NORMALIZAR_SALDO = cuenta ->
            cuenta.setSaldo(Optional.ofNullable(cuenta.getSaldo()).orElseGet(SALDO_POR_DEFECTO));

    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public Cuenta crear(Cuenta cuenta) {
        return Optional.ofNullable(cuenta)
                .map(c -> {
                    NORMALIZAR_SALDO.accept(c);
                    return c;
                })
                .map(cuentaRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta inválida"));
    }

    public Cuenta aplicarTransaccion(Long id, BigDecimal cantidad, BiFunction<BigDecimal, BigDecimal, BigDecimal> operacion) {
        Cuenta cuenta = buscarPorId(id);

        if (cuenta == null || cantidad == null || operacion == null) {
            return null;
        }

        cuenta.setSaldo(operacion.apply(cuenta.getSaldo(), cantidad));

        return cuentaRepository.save(cuenta);
    }

    public List<Cuenta> listar() {
        return cuentaRepository.findAll();
    }

    public Cuenta buscarPorId(Long id) {
        return Optional.ofNullable(id)
                .flatMap(cuentaRepository::findById)
                .orElse(null);
    }

    public void eliminar(Long id) {
        Optional.ofNullable(id).ifPresent(cuentaRepository::deleteById);
    }

    public Cuenta depositar(Long id, BigDecimal cantidad) {
        return Optional.ofNullable(id)
                .map(this::buscarPorId)
                .filter(CUENTA_VALIDA)
                .map(cuenta -> {
                    cuenta.setSaldo(cuenta.getSaldo().add(cantidad));
                    return cuentaRepository.save(cuenta);
                })
                .orElse(null);
    }

    public Cuenta retirar(Long id, BigDecimal cantidad) {
        return Optional.ofNullable(id)
                .map(this::buscarPorId)
                .filter(CUENTA_VALIDA)
                .filter(cuenta -> cuenta.getSaldo().compareTo(cantidad) >= 0)
                .map(cuenta -> {
                    cuenta.setSaldo(cuenta.getSaldo().subtract(cantidad));
                    return cuentaRepository.save(cuenta);
                })
                .orElse(null);
    }
}