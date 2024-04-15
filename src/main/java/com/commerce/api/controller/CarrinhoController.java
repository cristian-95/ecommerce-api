package com.commerce.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import com.commerce.api.exception.InvalidOperationException;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.CarrinhoDeCompras;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.CarrinhoDeComprasDTO;
import com.commerce.api.service.CarrinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "Carrinho De Compras", description = "Gerenciamento de carrinho de compras.")
@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService service;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exibe um carrinho de compras", description = "Consulta o banco de dados e retorna um determinado carrinho de compras, a partir do número de id passado na URI.", tags = {
            "Carrinho de Compras" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Produto.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<?> getById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        CarrinhoDeCompras carrinhoDeCompras = service.getCarrinhoDeComprasById(id);
        return ResponseEntity.ok(carrinhoDeCompras);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registra um carrinho de compras", description = "Instancia um novo objeto do tipo CarrinhoDeCompras e salva no banco de dados, a propriedade loja permanece nula inicialmente.", tags = {
            "Carrinho de Compras" }, responses = {
                    @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Produto.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<CarrinhoDeCompras> create(@RequestBody CarrinhoDeComprasDTO dto) {
        return new ResponseEntity<CarrinhoDeCompras>(service.createCarrinhoDeCompras(dto), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{id}/{operacao}/{produtoId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza um carrinho de compras", description = "Adiciona ou remove um produto do carrinho, sobrescreve um carrinho de compras existente com a mesma id e salva no banco de dados.", tags = {
            "Carrinho de Compras" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Produto.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<CarrinhoDeCompras> update(@PathVariable("id") Long id,
            @PathVariable("operacao") String operacao, @PathVariable("produtoId") Long produtoId)
            throws ResourceNotFoundException, InvalidOperationException {
        return ResponseEntity.ok(service.update(id, operacao, produtoId));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Remove um produto", description = "Consulta o banco de dados e remove um determinado produto do banco de dados, a partir do número de id passado na URI.", tags = {
            "Carrinho de Compras" }, responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Produto.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws Exception {
        service.deleteCarrinhoDeCompras(id);
        return ResponseEntity.noContent().build();
    }
}
