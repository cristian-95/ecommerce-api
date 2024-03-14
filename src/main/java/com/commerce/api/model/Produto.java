package com.commerce.api.model;

import java.io.Serializable;
import java.util.HashMap;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Produto implements Serializable{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String descricao;
    private HashMap<String, String> specs;
    private Double price;
    private Long qtdeEstoque;

  
    public Produto(Long id, String nome, HashMap<String, String> specs, Double price, Long qtdeEstoque, String descricao) {
        this.id = id;
        this.nome = nome;
        this.specs = specs;
        this.descricao = descricao;
        this.price = price;
        this.qtdeEstoque = qtdeEstoque;
    }

    public Produto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public HashMap<String, String> getSpecs() {
        return specs;
    }

    public void setSpecs(HashMap<String, String> specs) {
        this.specs = specs;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getQtdeEstoque() {
        return qtdeEstoque;
    }

    public void setQtdeEstoque(Long qtdeEstoque) {
        this.qtdeEstoque = qtdeEstoque;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
        result = prime * result + ((specs == null) ? 0 : specs.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((qtdeEstoque == null) ? 0 : qtdeEstoque.hashCode());
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
        Produto other = (Produto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (descricao == null) {
            if (other.descricao != null)
                return false;
        } else if (!descricao.equals(other.descricao))
            return false;
        if (specs == null) {
            if (other.specs != null)
                return false;
        } else if (!specs.equals(other.specs))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (qtdeEstoque == null) {
            if (other.qtdeEstoque != null)
                return false;
        } else if (!qtdeEstoque.equals(other.qtdeEstoque))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Produto [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", specs=" + specs + ", price="
                + price + ", qtdeEstoque=" + qtdeEstoque + "]";
    }

    
}
