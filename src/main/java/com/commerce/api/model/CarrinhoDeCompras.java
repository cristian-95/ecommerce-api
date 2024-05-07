package com.commerce.api.model;

import com.commerce.api.model.dto.CarrinhoDeComprasDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.util.HashMap;

@Entity
@Table(name = "carrinhos")
public class CarrinhoDeCompras {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private HashMap<Produto, Integer> items;

    @JsonBackReference
    @OneToOne(mappedBy = "carrinhoDeCompras")
    private Pedido pedido;

    @Min(0)
    private Double total;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    private Cliente cliente;

    public CarrinhoDeCompras() {
        this.items = new HashMap<>();
        this.total = 0.0;
    }

    public CarrinhoDeCompras(HashMap<Produto, Integer> items, Double total) {
        this.items = items;
        this.total = total;
    }

    public CarrinhoDeCompras(Long id, HashMap<Produto, Integer> items, Pedido pedido, Double total, Cliente cliente) {
        this.id = id;
        this.items = items;
        this.pedido = pedido;
        this.total = total;
        this.cliente = cliente;
    }

    public CarrinhoDeCompras(CarrinhoDeComprasDTO dto) {
        this.items = dto.items();
        this.total = dto.total();
    }

    public void adicionarItem(Produto produto) {
        if (items.containsKey(produto)) {
            items.put(produto, items.get(produto) + 1);
        } else {
            items.put(produto, 1);
        }
        this.atualizarTotal();
    }

    private void atualizarTotal() {
        Double res = 0.0;
        var produtosNoCarrinho = this.items.keySet();
        for (Produto produto : produtosNoCarrinho) {
            res += produto.getPreco();
        }
        setTotal(res);
    }

    public void removerItem(Produto produto) {
        if (items.containsKey(produto) && items.get(produto) > 1) {
            items.put(produto, items.get(produto) - 1);
        } else if (items.get(produto) == 1) {
            items.remove(produto);
        }
        this.atualizarTotal();
    }

    public void limparCarrinho() {
        this.items.clear();
    }

    public HashMap<Produto, Integer> getItems() {
        return items;
    }

    public void setItems(HashMap<Produto, Integer> items) {
        this.items = items;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        result = prime * result + ((pedido == null) ? 0 : pedido.hashCode());
        result = prime * result + ((total == null) ? 0 : total.hashCode());
        result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
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
        CarrinhoDeCompras other = (CarrinhoDeCompras) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (items == null) {
            if (other.items != null)
                return false;
        } else if (!items.equals(other.items))
            return false;
        if (pedido == null) {
            if (other.pedido != null)
                return false;
        } else if (!pedido.equals(other.pedido))
            return false;
        if (total == null) {
            if (other.total != null)
                return false;
        } else if (!total.equals(other.total))
            return false;
        if (cliente == null) {
            if (other.cliente != null)
                return false;
        } else if (!cliente.equals(other.cliente))
            return false;
        return true;
    }

}
