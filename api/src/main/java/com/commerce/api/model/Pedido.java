package com.commerce.api.model;

import com.commerce.api.utils.GeradorPedidoCodigo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pedidos")
@JsonPropertyOrder({"codigo", "status", "timestamp", "cliente", "loja", "total", "itens"})
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @Column(unique = true)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIncludeProperties({"nome", "sobrenome"})
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "loja_id")
    @JsonIncludeProperties({"nome"})
    private Loja loja;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "GMT-3")
    private LocalDateTime timestamp;
    private PedidoStatus status;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonIncludeProperties({"produto", "quantidade"})
    private List<Item> itens;

    @Column
    private Double total;

    public Pedido() {
        this.codigo = GeradorPedidoCodigo.novoCodigo();
        this.timestamp = LocalDateTime.now();
        this.status = PedidoStatus.PENDENTE;
    }

    public Pedido(Cliente cliente, Loja loja, List<Item> itens) {
        this.codigo = GeradorPedidoCodigo.novoCodigo();
        this.cliente = cliente;
        this.loja = loja;
        this.timestamp = LocalDateTime.now();
        this.status = PedidoStatus.PENDENTE;

        this.itens = itens;
        this.total = atualizarTotal();
    }

    public Double atualizarTotal() {
        Double res = 0.0;
        for (Item i : itens) {
            res += i.getProduto().getPreco() * i.getQuantidade();
        }
        return res;
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

    public List<Item> getItens() {
        return this.itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido pedido)) return false;
        return Objects.equals(id, pedido.id) && Objects.equals(codigo, pedido.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }
}
