package com.commerce.api.model.dto;

import com.commerce.api.model.Cliente;
import com.commerce.api.model.Item;
import com.commerce.api.model.Loja;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        String codigo,
        String status,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "GMT-3")
        LocalDateTime timestamp,
        @JsonIncludeProperties({"id", "nome", "sobrenome"})
        Cliente cliente,
        @JsonIncludeProperties({"id", "nome"})
        Loja loja,
        Double total,
        @JsonUnwrapped
        @JsonProperty("itens")
        List<Item> itens
) {
}
