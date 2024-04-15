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

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Produto;
import com.commerce.api.model.CarrinhoDeCompras;
import com.commerce.api.model.Cliente;
import com.commerce.api.model.Pedido;
import com.commerce.api.model.dto.ClienteDTO;
import com.commerce.api.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Clientes", description = "Gerenciamento de clientes.")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista todos os clientes", description = "Consulta o banco de dados e retorna todos os clientes.", tags = {
            "Clientes" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<List<Cliente>> getAll() {
        return ResponseEntity.ok(service.getAllClientes());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Exibe um cliente", description = "Consulta o banco de dados e retorna um cliente, a partir do número do ID passado na URI.", tags = {
            "Clientes" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Cliente usuario = service.getClienteById(id);
        if (usuario == null)
            return new ResponseEntity<>("Cliente não encontrado.", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registra um cliente", description = "Instancia um novo objeto do tipo Cliente e salva no banco de dados.", tags = {
            "Clientes" }, responses = {
                    @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<Cliente> create(@RequestBody ClienteDTO dto) {
        return new ResponseEntity<Cliente>(service.createCliente(dto), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza um cliente", description = "Instancia um novo objeto do tipo Cliente, sobreescreve um Cliente com a mesma ID, e salva no banco de dados.", tags = {
            "Clientes" }, responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<Cliente> update(@RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(service.updateCliente(dto));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Remove um cliente", description = "Encontra um Cliente com base no ID e remove do banco de dados.", tags = {
            "Clientes" }, responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<?> delete(@PathVariable("id") Long id) throws Exception {
        service.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}/carrinho", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista os  produtos no carrinho de  um cliente", description = "Consulta o banco de dados e retorna todos os produtos no carrinho de um cliente.", tags = {
            "Clientes" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<CarrinhoDeCompras> getCarrinho(@PathVariable("id") Long clienteID) {
        return ResponseEntity.ok(service.getCarrinho(clienteID));
    }

    @PostMapping(value = "/{id}/carrinho", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adiciona um produto ao carrinho de compras de  um cliente", description = "Utiliza o id passado o reponse body para adicionar um produto ao carrinho de compras de um cliente.", tags = {
            "Clientes" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<Produto> adicionarAoCarrinho(@PathVariable("id") Long clienteID, @RequestBody Long produtoId)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(service.adicionarAoCarrinho(clienteID, produtoId));
    }

    @DeleteMapping(value = "/{id}/carrinho", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remove um produto do carrinho de compras de um cliente", description = "Utiliza o id passado o reponse body para remover um produto do carrinho de compras de  um cliente.", tags = {
            "Clientes" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<Produto> removerDoCarrinho(@PathVariable("id") Long clienteID, @RequestBody Long produtoId)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(service.removerDoCarrinho(clienteID, produtoId));
    }

    @GetMapping(value = "/{id}/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista o histórico de pedidos de  um cliente", description = "Lista o histórico de pedidos de  um cliente", tags = {
            "Clientes" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Pedido.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<List<Pedido>> getAllPedidos(@PathVariable("id") Long clienteID) {
        return ResponseEntity.ok(service.getAllPedidos(clienteID));
    }

    @GetMapping(value = "/{id}/favoritos", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista os  produtos favoritos de  um cliente", description = "Consulta o banco de dados e retorna todos os produtos favoritos de um cliente.", tags = {
            "Clientes" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<List<Produto>> getAllFavoritos(@PathVariable("id") Long clienteID) {
        return ResponseEntity.ok(service.getAllFavoritos(clienteID));
    }

    @PostMapping(value = "/{id}/favoritos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adiciona um produto aos favoritos de  um cliente", description = "Utiliza o id passado o reponse body para adicionar um produto aos favoritos de  um cliente.", tags = {
            "Clientes" }, responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<Produto> adicionarFavorito(@PathVariable("id") Long clienteID, @RequestBody Long produtoId)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(service.adicionarFavorito(clienteID, produtoId));
    }

    @DeleteMapping(value = "/{id}/favoritos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remove um produto dos favoritos de  um cliente", description = "Utiliza o id passado o reponse body para remover um produto dos favoritos de  um cliente.", tags = {
            "Clientes" }, responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<Produto> removerFavorito(@PathVariable("id") Long clienteID, @RequestBody Long produtoId)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(service.removerFavorito(clienteID, produtoId));
    }
}
