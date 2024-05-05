package com.commerce.api.model;

import com.commerce.api.model.dto.LojaDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lojas")
public class Loja extends Usuario {

    private static final long serialVersionUID = 1L;

    @CNPJ
    private String CNPJ;

    @OneToMany(mappedBy = "loja")
    @JsonManagedReference
    private List<Produto> produtos;

    @OneToMany(mappedBy = "loja")
    @JsonManagedReference
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

    public Loja(String username, String email, String password, String nome, String telefone, String endereco,
                UserRole role, String cNPJ) {
        super(username, email, password, nome, telefone, endereco, role);
        CNPJ = cNPJ;
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

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String cNPJ) {
        CNPJ = cNPJ;
    }

    public List<Produto> getProdutos() {
        return this.produtos;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void adicionarProduto(Produto produto) {
        if (!this.produtos.contains(produto)) {
            this.produtos.add(produto);
        }
    }

    public void removerProduto(Produto produto) {
        if (!this.produtos.contains(produto)) {
            this.produtos.remove(produto);
        }
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((CNPJ == null) ? 0 : CNPJ.hashCode());
        result = prime * result + ((produtos == null) ? 0 : produtos.hashCode());
        result = prime * result + ((pedidos == null) ? 0 : pedidos.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Loja other = (Loja) obj;
        if (CNPJ == null) {
            if (other.CNPJ != null)
                return false;
        } else if (!CNPJ.equals(other.CNPJ))
            return false;
        if (produtos == null) {
            if (other.produtos != null)
                return false;
        } else if (!produtos.equals(other.produtos))
            return false;
        if (pedidos == null) {
            if (other.pedidos != null)
                return false;
        } else if (!pedidos.equals(other.pedidos))
            return false;
        return true;
    }

}