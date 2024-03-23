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

import com.commerce.api.model.Produto;
import com.commerce.api.model.Usuario;
import com.commerce.api.model.dto.UsuarioDTO;
import com.commerce.api.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Usuario>> getAll(){
        return ResponseEntity.ok(service.getAllUsuarios());        
    }

    @GetMapping(value = "/{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable("id")Long id){
        Usuario  usuario = service.getUsuarioById(id);
        if (usuario == null) return new ResponseEntity<>("Usuario não encontrado.", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> create(@RequestBody UsuarioDTO dto){
        return new ResponseEntity<Usuario>(service.createUsuario(dto), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Usuario> update(@RequestBody UsuarioDTO dto){
        return ResponseEntity.ok(service.updateUsuario(dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")Long id) throws Exception{
        service.deleteUsuario(id);
        return ResponseEntity.noContent().build();        
    }

    @GetMapping(value = "/{user_id}/favoritos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Produto>> getAllFavoritos(@PathVariable("user_id")Long userId){
        return ResponseEntity.ok(service.getAllFavoritos(userId));
    }

    @PostMapping(value = "/{user_id}/favoritos",
    produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> addFavorito(@PathVariable("user_id")Long userId, @RequestBody Long produtoId){
        return ResponseEntity.ok(service.addFavorito(userId,produtoId));
    }

    @DeleteMapping(value = "/{user_id}/favoritos",
    produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> removeFavorito(@PathVariable("user_id")Long userId, @RequestBody Long produtoId){
        return ResponseEntity.ok(service.removeFavorito(userId,produtoId));
    }
}
