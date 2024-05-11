package com.commerce.api.model;

public enum PedidoStatus {
    PENDENTE("pendente"),
    EM_PROGRESSO("em progresso"),
    CONCLUIDO("concluido"),
    CANCELADO("cancelado");

    private String status;

    PedidoStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
