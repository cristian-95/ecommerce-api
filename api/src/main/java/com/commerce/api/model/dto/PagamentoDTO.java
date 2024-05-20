package com.commerce.api.model.dto;

public record PagamentoDTO(
        Long idPagamento,
        String metodoPagamento,
        String status
) {
}
