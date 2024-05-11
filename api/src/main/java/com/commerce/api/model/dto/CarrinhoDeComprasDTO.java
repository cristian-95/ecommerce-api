package com.commerce.api.model.dto;

import com.commerce.api.model.Produto;

import java.util.HashMap;

public record CarrinhoDeComprasDTO(
        Long id,
        HashMap<Produto, Integer> items,
        Double total
) {
}
