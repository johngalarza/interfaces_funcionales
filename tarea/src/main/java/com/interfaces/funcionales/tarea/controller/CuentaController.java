package com.interfaces.funcionales.tarea.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.interfaces.funcionales.tarea.model.Cuenta;
import com.interfaces.funcionales.tarea.service.CuentaService;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    public ResponseEntity<Cuenta> crear(@RequestBody Cuenta cuenta) {

        Cuenta nuevaCuenta = cuentaService.crear(cuenta);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevaCuenta);
    }

    @GetMapping
    public ResponseEntity<List<Cuenta>> listar() {

        return ResponseEntity.ok(
                cuentaService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> buscar(@PathVariable Long id) {

        Cuenta cuenta = cuentaService.buscarPorId(id);

        if (cuenta == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cuenta);
    }

    @PutMapping("/{id}/depositar")
    public ResponseEntity<Cuenta> depositar(
            @PathVariable Long id,
            @RequestParam BigDecimal cantidad) {

        Cuenta cuenta = cuentaService.depositar(id, cantidad);

        if (cuenta == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(cuenta);
    }

    @PutMapping("/{id}/retirar")
    public ResponseEntity<Cuenta> retirar(
            @PathVariable Long id,
            @RequestParam BigDecimal cantidad) {

        Cuenta cuenta = cuentaService.retirar(id, cantidad);

        if (cuenta == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(cuenta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        Cuenta cuenta = cuentaService.buscarPorId(id);

        if (cuenta == null) {
            return ResponseEntity.notFound().build();
        }

        cuentaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
