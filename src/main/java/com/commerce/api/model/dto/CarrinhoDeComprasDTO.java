package com.commerce.api.model.dto;

import java.util.HashMap;

import com.commerce.api.model.Produto;

public record CarrinhoDeComprasDTO(
    Long id,
    HashMap<Produto, Integer> items,
    Double total
) {}
