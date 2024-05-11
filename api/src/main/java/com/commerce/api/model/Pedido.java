package com.commerce.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "loja_id")
    @JsonBackReference
    private Loja loja;

    private LocalDateTime timestamp;
    private PedidoStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "carrinho_id", referencedColumnName = "id")
    private CarrinhoDeCompras carrinhoDeCompras;
    private Double total;

    public Pedido() {
    }

    public Pedido(Cliente cliente, Loja loja, PedidoStatus status, CarrinhoDeCompras carrinhoDeCompras) {
        this.cliente = cliente;
        this.loja = loja;
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.carrinhoDeCompras = carrinhoDeCompras;
        this.total = carrinhoDeCompras.getTotal();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public PedidoStatus getStatus() {
        return status;
    }

    public void setStatus(PedidoStatus status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = PedidoStatus.valueOf(status);
    }

    public CarrinhoDeCompras getCarrinho() {
        return carrinhoDeCompras;
    }

    public void setCarrinho(CarrinhoDeCompras carrinhoDeCompras) {
        this.carrinhoDeCompras = carrinhoDeCompras;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
        result = prime * result + ((loja == null) ? 0 : loja.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((carrinhoDeCompras == null) ? 0 : carrinhoDeCompras.hashCode());
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
        Pedido other = (Pedido) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (cliente == null) {
            if (other.cliente != null)
                return false;
        } else if (!cliente.equals(other.cliente))
            return false;
        if (loja == null) {
            if (other.loja != null)
                return false;
        } else if (!loja.equals(other.loja))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (carrinhoDeCompras == null) {
            if (other.carrinhoDeCompras != null)
                return false;
        } else if (!carrinhoDeCompras.equals(other.carrinhoDeCompras))
            return false;
        return true;
    }
}
