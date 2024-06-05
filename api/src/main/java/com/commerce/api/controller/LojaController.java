package com.commerce.api.controller;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Cliente;
import com.commerce.api.model.Loja;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.LojaDTO;
import com.commerce.api.model.dto.LojaUpdateDTO;
import com.commerce.api.security.TokenService;
import com.commerce.api.service.LojaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private LojaService lojaService;
    @Autowired
    private TokenService tokenService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista todas as lojas", description = "Consulta o banco de dados e retorna todas as lojas.", tags = {
            "Lojas"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Loja.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<PagedModel<EntityModel<Loja>>> getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
        return ResponseEntity.ok(lojaService.getAllLojas(pageable));
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exibe o perfil da loja", description = "Consulta o banco de dados e retorna uma loja a partir do username.", tags = {
            "Lojas"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(lojaService.getProfile(username));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registra uma loja", description = "Instancia um novo objeto do tipo Loja e salva no banco de dados.", tags = {
            "Lojas"}, responses = {
            @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Loja.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Loja> create(@RequestBody @Valid LojaDTO dto) {
        return new ResponseEntity<>(lojaService.createLoja(dto), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza uma loja", description = "Instancia um novo objeto do tipo Loja, sobreescreve um Loja com a mesma ID, e salva no banco de dados.", tags = {
            "Lojas"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Loja.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Loja> update(@RequestHeader("Authorization") String token,
                                       @RequestBody @Valid LojaUpdateDTO dto) {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(lojaService.updateLoja(username, dto));
    }

    @DeleteMapping(value = "/profile")
    @Operation(summary = "Remove uma loja", description = "Encontra um Loja com base no ID e remove do banco de dados.", tags = {
            "Lojas"}, responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Loja.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token) throws Exception {
        String username = tokenService.getUsernameFromToken(token);
        lojaService.deleteLoja(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/produtos", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista os produtos de uma loja", description = "Consulta o banco de dados e retorna todos os produtos de uma loja.", tags = {
            "Lojas"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Loja.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
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
