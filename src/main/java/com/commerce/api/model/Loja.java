package com.commerce.api.model;

import java.util.ArrayList;
import java.util.List;

import com.commerce.api.model.dto.LojaDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "lojas")
public class Loja extends Usuario {

    private String CNPJ;
    @OneToMany(mappedBy = "loja")
    @JsonManagedReference
    private List<Produto> produtos;

    public Loja() {
    }

    public Loja(String username, String email, String password, String nome, String telefone, String endereco,
            String cNPJ) {
        super(username, email, password, nome, telefone, endereco);
        this.CNPJ = cNPJ;
        this.produtos = new ArrayList<>();
    }

    public Loja(LojaDTO dto) {
        super(dto);
        this.CNPJ = dto.CNPJ();
        this.produtos = new ArrayList<>();
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((CNPJ == null) ? 0 : CNPJ.hashCode());
        result = prime * result + ((produtos == null) ? 0 : produtos.hashCode());
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
        return true;
    }
}