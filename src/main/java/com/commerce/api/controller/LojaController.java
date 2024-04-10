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
import com.commerce.api.model.Loja;
import com.commerce.api.model.dto.LojaDTO;
import com.commerce.api.service.LojaService;

@RestController
@RequestMapping("/lojas")
public class LojaController {

    @Autowired
    private LojaService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Usuario>> getAll(){
        return ResponseEntity.ok(service.getAllLojas());        
    }

    @GetMapping(value = "/{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable("id")Long id){
        Loja  usuario = service.getLojaById(id);
        if (usuario == null) return new ResponseEntity<>("Loja não encontrado.", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Loja> create(@RequestBody LojaDTO dto){
        return new ResponseEntity<Loja>(service.createLoja(dto), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Loja> update(@RequestBody LojaDTO dto){
        return ResponseEntity.ok(service.updateLoja(dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")Long id) throws Exception{
        service.deleteLoja(id);
        return ResponseEntity.noContent().build();        
    }

    @GetMapping(value = "/{id}/produtos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Produto>> getAllProdutos(@PathVariable("id")Long lojaID){
        return ResponseEntity.ok(service.getAllProdutos(lojaID));
    }

    @PostMapping(value = "/{id}/produtos",
    produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProduto(@PathVariable("id")Long lojaID, @RequestBody Long produtoId) throws ResourceNotFoundException{
        service.addProduto(lojaID,produtoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/produtos",
    produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeProduto(@PathVariable("id")Long lojaID, @RequestBody Long produtoId) throws ResourceNotFoundException{
        service.removeProduto(lojaID,produtoId);
        return ResponseEntity.ok().build();
    }
}
