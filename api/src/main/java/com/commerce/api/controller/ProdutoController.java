package com.commerce.api.controller;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.ProdutoDTO;
import com.commerce.api.model.dto.RequestDTO;
import com.commerce.api.security.TokenService;
import com.commerce.api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    @Operation(summary = "Lista todos os produtos", tags = {"Produtos"})
    public ResponseEntity<PagedModel<EntityModel<Produto>>> getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "searchKey", defaultValue = "") String searchKey,
            HttpServletRequest request

    ) {
        var sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
        return ResponseEntity.ok(produtoService.getAllProdutos(pageable, searchKey, request));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exibe um produto", tags = {"Produtos"})
    public ResponseEntity<?> getById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Produto produto = produtoService.getProdutoById(id);
        if (produto == null)
            return new ResponseEntity<>("Produto não encontrado.", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(produto);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registra um produto", tags = {"Produtos"})
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
    @Operation(summary = "Atualiza um produto", tags = {"Produtos"})
    public ResponseEntity<Produto> update(@RequestHeader("Authorization") String token, @RequestBody @Valid ProdutoDTO dto) throws ResourceNotFoundException {
        token = token.replace("Bearer ", "");
        String username = tokenService.validateToken(token);
        return ResponseEntity.ok(produtoService.updateProduto(username, dto));
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remove um produto", tags = {"Produtos"})
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @RequestBody RequestDTO requestDTO) throws Exception {
        String username = tokenService.getUsernameFromToken(token);
        produtoService.deleteProduto(username, requestDTO);
        return ResponseEntity.noContent().build();
    }
}
