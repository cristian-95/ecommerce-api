package com.commerce.api.model.dto;

public record LojaDTO(
        Long id,
        String username,
        String email,
        String password,
        String telefone,
        String endereco,
        String nome,
        String CNPJ
){}
