package com.commerce.api.model;

import com.commerce.api.model.dto.LojaDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lojas")
@JsonPropertyOrder({"id", "username", "endereco", "email", "telefone", "nome", "cnpj", "pedidos", "produtos"})
public class Loja extends Usuario {

    @CNPJ
    private String CNPJ;

    @JsonIgnore
    @OneToMany(mappedBy = "loja")
    @JsonManagedReference
    private List<Produto> produtos;

    @Column(length = 1000)
    private String descricao;

    @JsonIgnore
    @OneToMany(mappedBy = "loja")
    private List<Pedido> pedidos;

    public Loja() {
        super();
        this.produtos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }

    public Loja(String username, String encriptedPassword, String role) {
        super(username, encriptedPassword, role);
        this.produtos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }

    public Loja(LojaDTO dto) {
        super(dto);
        this.CNPJ = dto.CNPJ();
        this.produtos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }

    public Loja(String username, String email, String password, String nome, String descricao, String telefone, String endereco,
                UserRole role, String cNPJ) {
        super(username, email, password, nome, telefone, endereco, role);
        CNPJ = cNPJ;
        this.descricao = descricao;
        this.produtos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
    }

    public Loja(String username, String email, String password, String nome, String telefone, String endereco,
                UserRole role, String cNPJ, List<Produto> produtos, List<Pedido> pedidos) {
        super(username, email, password, nome, telefone, endereco, role);
        CNPJ = cNPJ;
        this.produtos = produtos;
        this.pedidos = pedidos;
    }

    public void adicionarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String cNPJ) {
        CNPJ = cNPJ;
    }

    public List<Produto> getProdutos() {
        return this.produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}