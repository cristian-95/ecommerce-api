package com.commerce.api.model.dto;

public record PedidoDTO(
        Long clienteId,
        Long lojaId,
        Long carrinhoId
) {
}
