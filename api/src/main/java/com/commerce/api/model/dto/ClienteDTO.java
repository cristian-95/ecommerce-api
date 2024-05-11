package com.commerce.api.model.dto;

import com.commerce.api.validation.constraints.Telefone;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record ClienteDTO(
        Long id,
        @Column(unique = true)
        String username,
        @Email(message = "E-mail em formato inválido.")
        String email,
        String password,
        @Column(nullable = true)
        @Telefone
        String telefone,
        String endereco,
        String nome,
        String sobrenome,
        @CPF
        String CPF,
        LocalDate dataNasc,
        String genero
) {
}