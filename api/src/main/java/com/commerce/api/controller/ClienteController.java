package com.commerce.api.controller;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.CarrinhoDeCompras;
import com.commerce.api.model.Cliente;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.ClienteDTO;
import com.commerce.api.model.dto.ClienteUpdateDTO;
import com.commerce.api.model.dto.RequestDTO;
import com.commerce.api.security.TokenService;
import com.commerce.api.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clientes", description = "Gerenciamento de clientes.")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final TokenService tokenService;

    public ClienteController(ClienteService clienteService, TokenService tokenService) {
        this.clienteService = clienteService;
        this.tokenService = tokenService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista todos os clientes", tags = {"Clientes"})
    public ResponseEntity<PagedModel<EntityModel<Cliente>>> getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
        return ResponseEntity.ok(clienteService.getAllClientes(pageable));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registra um cliente", tags = {"Clientes"})
    public ResponseEntity<Cliente> create(@RequestHeader("Authorization") String token, @RequestBody @Valid ClienteDTO dto) throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return new ResponseEntity<>(clienteService.createCliente(username, dto), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza um cliente", tags = {"Clientes"})
    public ResponseEntity<Cliente> update(@RequestHeader(name = "Authorization") String token, @RequestBody @Valid ClienteUpdateDTO dto) throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.updateProfile(username, dto));
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exibe o perfil do cliente", tags = {"Clientes"})
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.getProfile(username));
    }

    @DeleteMapping(value = "/profile")
    @Operation(summary = "Remove um cliente", tags = {"Clientes"})
    public ResponseEntity<?> deletAccount(@RequestHeader("Authorization") String token) throws Exception {
        String username = tokenService.getUsernameFromToken(token);
        clienteService.deleteAccount(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/carrinho", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista os  produtos no carrinho de  um cliente", tags = {"Clientes"})
    public ResponseEntity<CarrinhoDeCompras> getCarrinho(@RequestHeader(name = "Authorization") String token) throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.getCarrinho(username));
    }

    @PostMapping(value = "/carrinho", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adiciona um produto ao carrinho de compras de  um cliente", tags = {"Clientes"})
    public ResponseEntity<CarrinhoDeCompras> adicionarAoCarrinho(@RequestHeader(name = "Authorization") String token, @RequestBody RequestDTO requestDTO)
            throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.adicionarAoCarrinho(username, requestDTO));
    }

    @DeleteMapping(value = "/carrinho", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remove um produto do carrinho de compras de um cliente", tags = {"Clientes"})
    public ResponseEntity<CarrinhoDeCompras> removerDoCarrinho(@RequestHeader(name = "Authorization") String token, @RequestBody RequestDTO requestDTO)
            throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.removerDoCarrinho(username, requestDTO));
    }

    @GetMapping(value = "/favoritos", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista os  produtos favoritos de  um cliente", tags = {"Clientes"})
    public ResponseEntity<List<Produto>> getAllFavoritos(@RequestHeader(name = "Authorization") String token) throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.getAllFavoritos(username));
    }

    @PostMapping(value = "/favoritos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adiciona um produto aos favoritos de  um cliente", tags = {"Clientes"})
    public ResponseEntity<Produto> adicionarFavorito(@RequestHeader(name = "Authorization") String token, @RequestBody Long produtoId)
            throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.adicionarFavorito(username, produtoId));
    }

    @DeleteMapping(value = "/favoritos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remove um produto dos favoritos de  um cliente", tags = {"Clientes"})
    public ResponseEntity<Produto> removerFavorito(@RequestHeader(name = "Authorization") String token, @RequestBody Long produtoId)
            throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.removerFavorito(username, produtoId));
    }

    public ResponseEntity<?> getById(Long id) {
        Cliente cliente;
        try {
            cliente = clienteService.getClienteById(id);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(cliente);
    }
}
