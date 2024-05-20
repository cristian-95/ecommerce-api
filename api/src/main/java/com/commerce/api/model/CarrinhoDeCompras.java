package com.commerce.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @OneToOne(mappedBy = "carrinhoDeCompras")
    private Pedido pedido;

    @Min(0)
    private Double total;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "loja_id")
    @JsonIgnore
    private Loja loja;

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
        this.itens = itens;
        this.pedido = pedido;
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

    public boolean removerItem(Item item) {
        boolean flag = false;
        if (itens.contains(item)) {
            flag = itens.get(itens.indexOf(item)).removerProduto();
            this.atualizarTotal();
        } else {
            System.out.println("Produto " + item.getProduto().getNome() + " não consta entre os itens.");
        }
        return flag;
    }

    public void limparCarrinho() {
        this.itens.clear();
        atualizarTotal();
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
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
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarrinhoDeCompras that)) return false;
        if (!super.equals(o)) return false;

        return Objects.equals(id, that.id) && Objects.equals(itens, that.itens) && Objects.equals(pedido, that.pedido) && Objects.equals(total, that.total) && Objects.equals(cliente, that.cliente);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(itens);
        result = 31 * result + Objects.hashCode(pedido);
        result = 31 * result + Objects.hashCode(total);
        result = 31 * result + Objects.hashCode(cliente);
        return result;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }
}