package com.commerce.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity(name = "item")
@Table(name = "itens")
@JsonPropertyOrder({"id", "quantidade", "produto"})
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    @JsonIncludeProperties({"id", "nome", "preco", "_links"})
    private Produto produto;

    @Min(0)
    private Integer quantidade;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "carrinho_de_compras_id")
    private CarrinhoDeCompras carrinhoDeCompras;

    @ManyToOne(targetEntity = Pedido.class)
    private Pedido pedido;

    public Item() {
    }

    public Item(CarrinhoDeCompras carrinhoDeCompras, Produto produto) {
        this.carrinhoDeCompras = carrinhoDeCompras;
        this.produto = produto;
        this.quantidade = 1;
    }


    public Item(Produto produto, Integer quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public void adicionarProduto() {
        this.quantidade++;
    }

    public boolean removerProduto() {
        this.quantidade--;
        return quantidade < 1;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public CarrinhoDeCompras getCarrinhoDeCompras() {
        return carrinhoDeCompras;
    }


    public void setCarrinhoDeCompras(CarrinhoDeCompras carrinhoDeCompras) {
        this.carrinhoDeCompras = carrinhoDeCompras;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", produto=" + produto +
                ", quantidade=" + quantidade +
                ", carrinhoDeCompras=" + carrinhoDeCompras +
                ", pedido=" + pedido +
                '}';
    }
}
