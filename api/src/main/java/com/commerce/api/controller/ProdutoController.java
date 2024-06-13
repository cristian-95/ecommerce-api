package com.commerce.api.controller;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.ProdutoDTO;
import com.commerce.api.model.dto.RequestDTO;
import com.commerce.api.security.TokenService;
import com.commerce.api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Produtos", description = "Gerenciamento de produtos.")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final TokenService tokenService;

    public ProdutoController(ProdutoService produtoService, TokenService tokenService) {
        this.produtoService = produtoService;
        this.tokenService = tokenService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista todos os produtos", description = "Consulta o banco de dados e retorna todos os produtos.", tags = {
            "Produtos"},
            responses = {
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
    public ResponseEntity<PagedModel<EntityModel<Produto>>> getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "searchKey", defaultValue = "") String searchKey
    ) {
        var sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
        return ResponseEntity.ok(produtoService.getAllProdutos(pageable, searchKey));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exibe um produto", description = "Consulta o banco de dados e retorna um determinado produto, a partir do número de id passado na URI.", tags = {"Produtos"})
    public ResponseEntity<?> getById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Produto produto = produtoService.getProdutoById(id);
        if (produto == null)
            return new ResponseEntity<>("Produto não encontrado.", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(produto);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Registra um produto", description = "Instancia um novo objeto do tipo Produto e salva no banco de dados, a propriedade loja permanece nula inicialmente.", tags = {
            "Produtos"}, responses = {
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
    public ResponseEntity<Produto> create(
            @RequestHeader("Authorization") String token,
            @RequestPart("produto") ProdutoDTO produto,
            @RequestPart("imagens") MultipartFile[] imagens
    ) {
        token = token.replace("Bearer ", "");
        String username = tokenService.validateToken(token);
        return new ResponseEntity<>(produtoService.createProduto(username, produto, imagens), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza um produto", description = "Instancia um novo objeto do tipo Produto, sobrescreve um produto existente com a mesma id e salva no banco de dados.", tags = {
            "Produtos"}, responses = {
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
    public ResponseEntity<Produto> update(@RequestHeader("Authorization") String token, @RequestBody @Valid ProdutoDTO dto) throws ResourceNotFoundException {
        token = token.replace("Bearer ", "");
        String username = tokenService.validateToken(token);
        return ResponseEntity.ok(produtoService.updateProduto(username, dto));
    }

    @DeleteMapping
    @Operation(summary = "Remove um produto", description = "Consulta o banco de dados e remove um determinado produto do banco de dados, a partir do número de id passado na URI.", tags = {
            "Produtos"}, responses = {
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
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @RequestBody RequestDTO requestDTO) throws Exception {
        String username = tokenService.getUsernameFromToken(token);
        produtoService.deleteProduto(username, requestDTO);
        return ResponseEntity.noContent().build();
    }
}
