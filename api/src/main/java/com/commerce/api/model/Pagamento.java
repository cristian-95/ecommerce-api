package com.commerce.api.model;

import com.commerce.api.model.dto.PagamentoDTO;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "pagamentos")
public class Pagamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPagamento;
    private String metodoPagamento;
    private String status;

    public Pagamento() {
    }

    public Pagamento(Long idPagamento, String metodoPagamento, String status) {
        this.idPagamento = idPagamento;
        this.metodoPagamento = metodoPagamento;
        this.status = status;
    }

    public Pagamento(PagamentoDTO dto) {
        this.idPagamento = dto.idPagamento();
        this.metodoPagamento = dto.metodoPagamento();
        this.status = dto.status();
    }

    public Long getIdPagamento() {
        return idPagamento;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setIdPagamento(Long idPagamento) {
        this.idPagamento = idPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idPagamento == null) ? 0 : idPagamento.hashCode());
        result = prime * result + ((metodoPagamento == null) ? 0 : metodoPagamento.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pagamento other = (Pagamento) obj;
        if (idPagamento == null) {
            if (other.idPagamento != null)
                return false;
        } else if (!idPagamento.equals(other.idPagamento))
            return false;
        if (metodoPagamento == null) {
            if (other.metodoPagamento != null)
                return false;
        } else if (!metodoPagamento.equals(other.metodoPagamento))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        return true;
    }
} 