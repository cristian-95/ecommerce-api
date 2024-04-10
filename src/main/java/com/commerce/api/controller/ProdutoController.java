package com.commerce.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.ProdutoDTO;
import com.commerce.api.service.ProdutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "Produtos", description = "Gerenciamento de produtos.")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista todos os produtos", description = "Consulta o banco de dados e retorna todos os produtos.", tags = {
            "Produtos" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", 
                    content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Produto.class))
                    )),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<List<Produto>> getAll() {
        return ResponseEntity.ok(service.getAllProdutos());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exibe um produto", description = "Consulta o banco de dados e retorna um determinado produto, a partir do número de id passado na URI.", tags = {
        "Produtos" }, responses = {
                @ApiResponse(description = "Success", responseCode = "200", 
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Produto.class))
                )),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        })
    public ResponseEntity<?> getById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Produto produto = service.getProdutoById(id);
        if (produto == null)
            return new ResponseEntity<>("Produto não encontrado.", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(produto);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registra um produto", description = "Instancia um novo objeto do tipo Produto e salva no banco de dados, a propriedade loja permanece nula inicialmente.", tags = {
        "Produtos" }, responses = {
                @ApiResponse(description = "Created", responseCode = "201", 
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Produto.class))
                )),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),                
                @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        })
    public ResponseEntity<Produto> create(@RequestBody ProdutoDTO dto) {
        return new ResponseEntity<Produto>(service.createProduto(dto), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza um produto", description = "Instancia um novo objeto do tipo Produto, sobrescreve um produto existente com a mesma id e salva no banco de dados.", tags = {
        "Produtos" }, responses = {
                @ApiResponse(description = "Success", responseCode = "200", 
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Produto.class))
                )),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),   
                @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),             
                @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        })
    public ResponseEntity<Produto> update(@RequestBody ProdutoDTO dto) throws ResourceNotFoundException {
        return ResponseEntity.ok(service.updateProduto(dto));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Remove um produto", description = "Consulta o banco de dados e remove um determinado produto do banco de dados, a partir do número de id passado na URI.", tags = {
        "Produtos" }, responses = {
                @ApiResponse(description = "No Content", responseCode = "204", 
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Produto.class))
                )),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),   
                @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),             
                @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
        })
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws Exception {
        service.deleteProduto(id);
        return ResponseEntity.noContent().build();
    }
}
