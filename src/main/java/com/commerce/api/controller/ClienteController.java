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
import com.commerce.api.model.Usuario;
import com.commerce.api.model.Cliente;
import com.commerce.api.model.dto.ClienteDTO;
import com.commerce.api.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Usuario>> getAll(){
        return ResponseEntity.ok(service.getAllClientes());        
    }

    @GetMapping(value = "/{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable("id")Long id){
        Cliente  usuario = service.getClienteById(id);
        if (usuario == null) return new ResponseEntity<>("Cliente não encontrado.", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cliente> create(@RequestBody ClienteDTO dto){
        return new ResponseEntity<Cliente>(service.createCliente(dto), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cliente> update(@RequestBody ClienteDTO dto){
        return ResponseEntity.ok(service.updateCliente(dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")Long id) throws Exception{
        service.deleteCliente(id);
        return ResponseEntity.noContent().build();        
    }

    @GetMapping(value = "/{id}/favoritos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Produto>> getAllFavoritos(@PathVariable("id")Long clienteID){
        return ResponseEntity.ok(service.getAllFavoritos(clienteID));
    }

    @PostMapping(value = "/{id}/favoritos",
    produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> addFavorito(@PathVariable("id")Long clienteID, @RequestBody Long produtoId) throws ResourceNotFoundException{
        return ResponseEntity.ok(service.addFavorito(clienteID,produtoId));
    }

    @DeleteMapping(value = "/{id}/favoritos",
    produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> removeFavorito(@PathVariable("id")Long clienteID, @RequestBody Long produtoId) throws ResourceNotFoundException{
        return ResponseEntity.ok(service.removeFavorito(clienteID,produtoId));
    }
}
