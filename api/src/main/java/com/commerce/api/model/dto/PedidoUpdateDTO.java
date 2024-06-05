package com.commerce.api.model.dto;

import com.commerce.api.validation.constraints.Status;

public record PedidoUpdateDTO(        
        String codigo,
        @Status
        String status
) {
}
