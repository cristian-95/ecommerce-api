package com.commerce.api.controller;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Pedido;
import com.commerce.api.model.dto.PedidoUpdateDTO;
import com.commerce.api.model.dto.RequestDTO;
import com.commerce.api.security.TokenService;
import com.commerce.api.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pedidos", description = "Gerenciamento de pedidos.")
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private TokenService tokenService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista todos os pedidos do perfil", description = "Consulta o banco de dados e retorna todos os pedidos do perfil.", tags = {
            "Pedidos"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Pedido.class))
                    )),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<List<Pedido>> getAll(@RequestHeader("Authorization") String token) {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(pedidoService.getAllPedidos(username));
    }

    @GetMapping(value = "/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exibe um pedido", description = "Consulta o banco de dados e retorna um determinado pedido, a partir do codigo.", tags = {
            "Pedidos"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Pedido.class))
                    )),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Pedido> getByCodigo(@PathVariable("codigo") String codigo) throws ResourceNotFoundException {
        return ResponseEntity.ok(this.pedidoService.getPedidoByCodigo(codigo));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Processa pedidos", description = "Itera os itens no carrinho e gera pedidos para cada loja.", tags = {
            "Pedidos"}, responses = {
            @ApiResponse(description = "Created", responseCode = "201",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Pedido.class))
                    )),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<?> processarPedidos(@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return new ResponseEntity<>(pedidoService.processarPedidos(username), HttpStatus.CREATED);
    }

    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza um pedido", description = "Modifica o status de um pedido.", tags = {
            "Pedidos"}, responses = {
            @ApiResponse(description = "Created", responseCode = "201",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Pedido.class))
                    )),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Pedido> update(@RequestHeader("Authorization") String token, @Valid @RequestBody PedidoUpdateDTO dto) throws IllegalArgumentException {
        String username = tokenService.getUsernameFromToken(token);
        return new ResponseEntity<>(pedidoService.updatePedido(username, dto), HttpStatus.CREATED);
    }

    @DeleteMapping
    @Operation(summary = "Remove um pedido", description = "Consulta o banco de dados e remove um determinado pedido do banco de dados, a partir do número de id passado na URI.", tags = {
            "Pedidos"}, responses = {
            @ApiResponse(description = "No Content", responseCode = "204",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Pedido.class))
                    )),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @RequestBody RequestDTO requestDTO) throws Exception {
        String username = tokenService.getUsernameFromToken(token);
        pedidoService.deletePedido(username, requestDTO);
        return ResponseEntity.noContent().build();
    }
}
