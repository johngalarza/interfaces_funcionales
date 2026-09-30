package com.interfaces.funcionales.tarea.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.interfaces.funcionales.tarea.model.Cliente;
import com.interfaces.funcionales.tarea.repository.ClienteRepository;

@Service
public class ClienteService {

    private static final Predicate<Cliente> CLIENTE_VALIDO = cliente ->
            cliente != null
                    && cliente.getName() != null && !cliente.getName().isBlank()
                    && cliente.getIdentificacion() != null && !cliente.getIdentificacion().isBlank()
                    && cliente.getEmail() != null && cliente.getEmail().contains("@");

    private static final Consumer<Cliente> NORMALIZAR_CLIENTE = cliente -> {
        if (cliente.getName() != null) {
            cliente.setName(cliente.getName().trim());
        }
        if (cliente.getIdentificacion() != null) {
            cliente.setIdentificacion(cliente.getIdentificacion().trim());
        }
        if (cliente.getEmail() != null) {
            cliente.setEmail(cliente.getEmail().trim().toLowerCase());
        }
    };

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente crear(Cliente cliente) {
        return Optional.ofNullable(cliente)
                .filter(CLIENTE_VALIDO)
                .map(c -> {
                    NORMALIZAR_CLIENTE.accept(c);
                    return c;
                })
                .map(clienteRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Cliente inválido"));
    }

    public String resumirCliente(Cliente cliente, Function<Cliente, String> formateador) {
        return Optional.ofNullable(cliente)
                .filter(CLIENTE_VALIDO)
                .map(formateador::apply)
                .orElse("Cliente sin información");
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return Optional.ofNullable(id)
                .flatMap(clienteRepository::findById)
                .orElse(null);
    }

    public void eliminar(Long id) {
        Optional.ofNullable(id).ifPresent(clienteRepository::deleteById);
    }
}