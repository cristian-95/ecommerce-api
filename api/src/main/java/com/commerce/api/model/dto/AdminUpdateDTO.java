package com.commerce.api.model.dto;

import com.commerce.api.validation.constraints.Telefone;
import com.commerce.api.validation.constraints.Username;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;

public record AdminUpdateDTO(
        @Column(unique = true)
        @Username
        String username,
        @Email(message = "E-mail em formato inválido.")
        String email,
        @Column(nullable = true)
        @Telefone
        String telefone,
        String endereco,
        String nome
) {
}
