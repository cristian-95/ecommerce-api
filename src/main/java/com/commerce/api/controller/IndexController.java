package com.commerce.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController {

    @GetMapping("/{name}")
    public ResponseEntity<?> hello(@PathVariable("name") String name){
        String msg = "<h1>Olá "+name+"</h1>";
        return ResponseEntity.ok(msg);
    }    
}
