package com.commerce.api.controller;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Loja;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.LojaDTO;
import com.commerce.api.model.dto.LojaUpdateDTO;
import com.commerce.api.security.TokenService;
import com.commerce.api.service.LojaService;
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

@Tag(name = "Lojas", description = "Gerenciamento de lojas.")
@RestController
@RequestMapping("/lojas")
public class LojaController {

    private final LojaService lojaService;
    private final TokenService tokenService;

    public LojaController(LojaService lojaService, TokenService tokenService) {
        this.lojaService = lojaService;
        this.tokenService = tokenService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista todas as lojas", tags = {"Lojas"})
    public ResponseEntity<PagedModel<EntityModel<Loja>>> getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "searchKey", defaultValue = "") String searchKey) {
        var sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
        return ResponseEntity.ok(lojaService.getAllLojas(pageable, searchKey));
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exibe o perfil da loja", tags = {"Lojas"})
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(lojaService.getProfile(username));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registra uma loja", tags = {"Lojas"})
    public ResponseEntity<Loja> create(@RequestBody @Valid LojaDTO dto) {
        return new ResponseEntity<>(lojaService.createLoja(dto), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza uma loja", tags = {"Lojas"})
    public ResponseEntity<Loja> update(@RequestHeader("Authorization") String token,
                                       @RequestBody @Valid LojaUpdateDTO dto) {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(lojaService.updateLoja(username, dto));
    }

    @DeleteMapping(value = "/profile")
    @Operation(summary = "Remove uma loja", tags = {"Lojas"})
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token) throws Exception {
        String username = tokenService.getUsernameFromToken(token);
        lojaService.deleteLoja(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/produtos", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista os produtos de uma loja", tags = {"Lojas"})
    public ResponseEntity<PagedModel<EntityModel<Produto>>> getAllProdutos(
            @RequestHeader(name = "Authorization") String token) {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(lojaService.getAllProdutos(username));
    }

    public ResponseEntity<?> getById(Long id) {
        Loja loja;
        try {
            loja = lojaService.getLojaById(id);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(loja);
    }
}
