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
import com.commerce.api.model.dto.ProdutoDTO;
import com.commerce.api.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Produto>> getAll(){
        return ResponseEntity.ok(service.getAllProdutos());        
    }

    @GetMapping(value = "/{id}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable("id")Long id) throws ResourceNotFoundException{
        Produto  produto = service.getProdutoById(id);
        if (produto == null) return new ResponseEntity<>("Produto não encontrado.", HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(produto);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> create(@RequestBody ProdutoDTO dto){
        return new ResponseEntity<Produto>(service.createProduto(dto), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> update(@RequestBody ProdutoDTO dto) throws ResourceNotFoundException{
        return ResponseEntity.ok(service.updateProduto(dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")Long id) throws Exception{
        service.deleteProduto(id);
        return ResponseEntity.noContent().build();        
    }
}
