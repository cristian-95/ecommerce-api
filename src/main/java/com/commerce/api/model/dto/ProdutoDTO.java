package com.commerce.api.model.dto;

import java.util.HashMap;

public record ProdutoDTO(
    Long id,
    String nome,
    String descricao,
    HashMap<String, String> specs,
    Double preco,
    Long qtdeEstoque
) {}
