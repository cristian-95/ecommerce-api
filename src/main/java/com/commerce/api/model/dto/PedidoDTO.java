package com.commerce.api.model.dto;

import java.time.LocalDateTime;

public record PedidoDTO(
    Long id, 
    Long clienteId, 
    Long lojaId, 
    LocalDateTime timestamp, 
    String status, 
    Long carrinhoId
) {}
