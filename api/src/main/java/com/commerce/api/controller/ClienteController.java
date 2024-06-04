package com.commerce.api.controller;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.CarrinhoDeCompras;
import com.commerce.api.model.Cliente;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.ClienteDTO;
import com.commerce.api.model.dto.ClienteUpdateDTO;
import com.commerce.api.security.TokenService;
import com.commerce.api.service.CarrinhoDeComprasService;
import com.commerce.api.service.ClienteService;
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

import java.util.List;

@Tag(name = "Clientes", description = "Gerenciamento de clientes.")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private CarrinhoDeComprasService carrinhoDeComprasService;
    @Autowired
    private TokenService tokenService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista todos os clientes", description = "Consulta o banco de dados e retorna todos os clientes.", tags = {
            "Clientes"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<PagedModel<EntityModel<Cliente>>> getAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "DESC".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
        return ResponseEntity.ok(clienteService.getAllClientes(pageable));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exibe um cliente", description = "Consulta o banco de dados e retorna um cliente, a partir do número do ID passado na URI.", tags = {
            "Clientes"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Cliente cliente;
        try {
            cliente = clienteService.getClienteById(id);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(cliente);
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exibe o perfil do cliente", description = "Consulta o banco de dados e retorna um cliente a partir do username.", tags = {
            "Clientes"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.getProfile(username));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registra um cliente", description = "Instancia um novo objeto do tipo Cliente e salva no banco de dados.", tags = {
            "Clientes"}, responses = {
            @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Cliente> create(@RequestBody @Valid ClienteDTO dto) throws ResourceNotFoundException {
        return new ResponseEntity<>(clienteService.createCliente(dto), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza um cliente", description = "Instancia um novo objeto do tipo Cliente, sobreescreve um Cliente com a mesma ID, e salva no banco de dados.", tags = {
            "Clientes"}, responses = {
            @ApiResponse(description = "Sucess", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Cliente> update(@RequestHeader(name = "Authorization") String token, @RequestBody @Valid ClienteUpdateDTO dto) throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.updateCliente(username, dto));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Remove um cliente", description = "Encontra um Cliente com base no ID e remove do banco de dados.", tags = {
            "Clientes"}, responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws Exception {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/carrinho", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista os  produtos no carrinho de  um cliente", description = "Consulta o banco de dados e retorna todos os produtos no carrinho de um cliente.", tags = {
            "Clientes"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<CarrinhoDeCompras> getCarrinho(@RequestHeader(name = "Authorization") String token) throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.getCarrinho(username));
    }

    @PostMapping(value = "/carrinho", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adiciona um produto ao carrinho de compras de  um cliente", description = "Utiliza o id passado o reponse body para adicionar um produto ao carrinho de compras de um cliente.", tags = {
            "Clientes"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<CarrinhoDeCompras> adicionarAoCarrinho(@RequestHeader(name = "Authorization") String token, @RequestBody Long produtoId)
            throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.adicionarAoCarrinho(username, produtoId));
    }

    @DeleteMapping(value = "/carrinho", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remove um produto do carrinho de compras de um cliente", description = "Utiliza o id passado o reponse body para remover um produto do carrinho de compras de  um cliente.", tags = {
            "Clientes"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<CarrinhoDeCompras> removerDoCarrinho(@RequestHeader(name = "Authorization") String token, @RequestBody Long produtoId)
            throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.removerDoCarrinho(username, produtoId));
    }

    @GetMapping(value = "/favoritos", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista os  produtos favoritos de  um cliente", description = "Consulta o banco de dados e retorna todos os produtos favoritos de um cliente.", tags = {
            "Clientes"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<List<Produto>> getAllFavoritos(@RequestHeader(name = "Authorization") String token) throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.getAllFavoritos(username));
    }

    @PostMapping(value = "/favoritos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adiciona um produto aos favoritos de  um cliente", description = "Utiliza o id passado o reponse body para adicionar um produto aos favoritos de  um cliente.", tags = {
            "Clientes"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Produto> adicionarFavorito(@RequestHeader(name = "Authorization") String token, @RequestBody Long produtoId)
            throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.adicionarFavorito(username, produtoId));
    }

    @DeleteMapping(value = "/favoritos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remove um produto dos favoritos de  um cliente", description = "Utiliza o id passado o reponse body para remover um produto dos favoritos de  um cliente.", tags = {
            "Clientes"}, responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<Produto> removerFavorito(@RequestHeader(name = "Authorization") String token, @RequestBody Long produtoId)
            throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(clienteService.removerFavorito(username, produtoId));
    }
}
