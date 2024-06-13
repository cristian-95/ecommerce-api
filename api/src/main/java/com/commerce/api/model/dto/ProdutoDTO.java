package com.commerce.api.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.HashMap;

public record ProdutoDTO(
        Long id,
        @Size(min = 2, max = 80)
        String nome,
        String descricao,
        HashMap<String, String> specs,
        Double preco,
        @Min(0)
        Integer qtdeEstoque,
        Long lojaId
) {
}
