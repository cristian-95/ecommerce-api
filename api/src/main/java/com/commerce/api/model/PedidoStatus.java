package com.commerce.api.model;

public enum PedidoStatus {
    PENDENTE("pendente"),
    EM_PROGRESSO("em_progresso"),
    CONCLUIDO("concluido"),
    CANCELADO("cancelado");

    private final String status;

    PedidoStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
