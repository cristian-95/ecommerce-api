package com.commerce.api.controller;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Pedido;
import com.commerce.api.model.dto.PedidoUpdateDTO;
import com.commerce.api.model.dto.RequestDTO;
import com.commerce.api.security.TokenService;
import com.commerce.api.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pedidos", description = "Gerenciamento de pedidos")
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final TokenService tokenService;

    public PedidoController(PedidoService pedidoService, TokenService tokenService) {
        this.pedidoService = pedidoService;
        this.tokenService = tokenService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista todos os pedidos do perfil", tags = {"Pedidos"})
    public ResponseEntity<List<Pedido>> getAll(@RequestHeader("Authorization") String token) {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(pedidoService.getAllPedidos(username));
    }

    @GetMapping(value = "/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exibe  um pedido", tags = {"Pedidos"})
    public ResponseEntity<Pedido> getByCodigo(@PathVariable("codigo") String codigo) throws ResourceNotFoundException {
        return ResponseEntity.ok(this.pedidoService.getPedidoByCodigo(codigo));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Processa pedidos", tags = {"Pedidos"})
    public ResponseEntity<?> processarPedidos(@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return new ResponseEntity<>(pedidoService.processarPedidos(username), HttpStatus.CREATED);
    }

    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza um pedido", tags = {"Pedidos"})
    public ResponseEntity<Pedido> update(@RequestHeader("Authorization") String token, @Valid @RequestBody PedidoUpdateDTO dto) throws IllegalArgumentException {
        String username = tokenService.getUsernameFromToken(token);
        return new ResponseEntity<>(pedidoService.updatePedido(username, dto), HttpStatus.CREATED);
    }

    @DeleteMapping
    @Operation(summary = "Remove um pedido", tags = {"Pedidos"})
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @RequestBody RequestDTO requestDTO) throws Exception {
        String username = tokenService.getUsernameFromToken(token);
        pedidoService.deletePedido(username, requestDTO);
        return ResponseEntity.noContent().build();
    }
}
