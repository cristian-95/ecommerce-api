package com.commerce.api.model.dto;

import java.time.LocalDate;

public record UsuarioDTO(
        Long id,
        String username,
        String password,
        String nome,
        String sobrenome,
        String documento,
        String email,
        String endereco,
        LocalDate dataNasc,
        String genero,
        String role
){}
