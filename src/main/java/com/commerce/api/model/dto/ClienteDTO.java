package com.commerce.api.model.dto;

import java.time.LocalDate;

public record ClienteDTO(
    Long id,
    String username,
    String email,
    String password,
    String telefone,
    String endereco,
    String nome,
    String sobrenome,
    String CPF,
    LocalDate dataNasc,
    String genero
) {}