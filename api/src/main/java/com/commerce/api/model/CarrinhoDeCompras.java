package com.commerce.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "carrinho_de_compras")
@JsonPropertyOrder({"id", "total", "itens"})
public class CarrinhoDeCompras {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "carrinhoDeCompras", cascade = CascadeType.REMOVE)
    private List<Item> itens;

    @Min(0)
    private Double total;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private Cliente cliente;


    public CarrinhoDeCompras() {
        this.itens = new ArrayList<>();
        this.total = 0.0;
    }

    public CarrinhoDeCompras(List<Item> itens) {
        this.itens = initializeItens(itens);
        atualizarTotal();
    }

    public CarrinhoDeCompras(Cliente cliente) {
        this.itens = new ArrayList<>();
        this.cliente = cliente;
        this.total = 0.0;
    }

    public CarrinhoDeCompras(Long id, List<Item> itens, Pedido pedido, Double total, Cliente cliente) {
        this.id = id;
        this.itens = initializeItens(itens);
        this.total = total;
        this.cliente = cliente;
    }

    private List<Item> initializeItens(List<Item> itens) {
        for (Item i : itens) {
            i.setCarrinhoDeCompras(this);
        }
        return itens;
    }

    public boolean containsProduto(Long id) {
        for (Item i : itens) {
            if (Objects.equals(i.getProduto().getId(), id)) return true;
        }
        return false;
    }

    public Item getItemByProdutoId(Long id) {
        for (Item i : itens) {
            if (Objects.equals(i.getProduto().getId(), id)) return i;
        }
        return null;
    }


    public Item adicionarItem(Produto produto) {
        for (int i = 0; i < itens.size(); i++) {

            if (produto.equals(itens.get(i).getProduto())) {
                itens.get(i).adicionarProduto();
                atualizarTotal();
                return itens.get(i);
            }
        }
        this.itens.add(new Item(this, produto));
        atualizarTotal();
        return itens.get(itens.size() - 1);
    }

    private void atualizarTotal() {
        double res = 0.0;
        for (Item i : itens) {
            res += i.getProduto().getPreco() * i.getQuantidade();
        }
        setTotal(res);
    }

    public boolean removerItem(Produto produto) {
        for (Item item : itens) {
            if (produto.equals(item.getProduto())) {
                item.removerProduto();
                atualizarTotal();
                return item.getQuantidade() == 0;
            }
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Item> getItens() {
        return itens;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarrinhoDeCompras that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}