package com.commerce.api.controller;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Pedido;
import com.commerce.api.model.dto.PedidoDTO;
import com.commerce.api.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private PedidoService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista todos os pedidos", description = "Consulta o banco de dados e retorna todos os pedidos.", tags = {
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
    public ResponseEntity<List<Pedido>> getAll() {
        return ResponseEntity.ok(service.getAllPedidos());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exibe um pedido", description = "Consulta o banco de dados e retorna um determinado pedido, a partir do número de id passado na URI.", tags = {
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
    public ResponseEntity<?> getById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Pedido pedido = service.getPedidoById(id);
        if (pedido == null)
            return new ResponseEntity<>("Pedido não encontrado.", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(pedido);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registra um pedido", description = "Instancia um novo objeto do tipo Pedido e salva no banco de dados, a propriedade loja permanece nula inicialmente.", tags = {
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
    public ResponseEntity<Pedido> create(@RequestBody PedidoDTO dto) throws ResourceNotFoundException {
        return new ResponseEntity<>(service.createPedido(dto), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
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
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws Exception {
        service.deletePedido(id);
        return ResponseEntity.noContent().build();
    }
}
