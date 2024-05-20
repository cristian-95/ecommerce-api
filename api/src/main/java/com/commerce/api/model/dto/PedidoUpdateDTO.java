package com.commerce.api.model.dto;

import com.commerce.api.validation.constraints.Status;

public record PedidoUpdateDTO(
        @Status
        String status
) {
}
