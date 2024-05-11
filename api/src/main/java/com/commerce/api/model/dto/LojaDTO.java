package com.commerce.api.model.dto;

import com.commerce.api.validation.constraints.Telefone;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.br.CNPJ;

public record LojaDTO(
        Long id,
        String username,
        @Email(message = "E-mail em formato inválido.")
        String email,
        String password,
        @Column(nullable = true)
        @Telefone
        String telefone,
        String endereco,
        String nome,
        @CNPJ
        String CNPJ
) {
}
