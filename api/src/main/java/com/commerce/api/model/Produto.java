package com.commerce.api.model;

import com.commerce.api.model.dto.ProdutoDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "produtos")
public class Produto extends RepresentationModel<Produto> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 2, max = 80)
    private String nome;

    private String descricao;
    private HashMap<String, String> specs;
    private Double preco;

    @Min(0)
    private Long qtdeEstoque;

    @JsonIgnore
    @ManyToMany(mappedBy = "favoritos")
    private List<Cliente> clientes;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "loja_id")
    private Loja loja;

    public Produto() {
    }

    public Produto(Long id, String nome, HashMap<String, String> specs, Double preco, Long qtdeEstoque,
                   String descricao) {
        this.id = id;
        this.nome = nome;
        this.specs = specs;
        this.descricao = descricao;
        this.preco = preco;
        this.qtdeEstoque = qtdeEstoque;
    }

    public Produto(ProdutoDTO dto) {
        this.id = dto.id();
        this.nome = dto.nome();
        this.specs = dto.specs();
        this.descricao = dto.descricao();
        this.preco = dto.preco();
        this.qtdeEstoque = dto.qtdeEstoque();
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

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Long getQtdeEstoque() {
        return qtdeEstoque;
    }

    public void setQtdeEstoque(Long qtdeEstoque) {
        this.qtdeEstoque = qtdeEstoque;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
        result = prime * result + ((specs == null) ? 0 : specs.hashCode());
        result = prime * result + ((preco == null) ? 0 : preco.hashCode());
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
        if (preco == null) {
            if (other.preco != null)
                return false;
        } else if (!preco.equals(other.preco))
            return false;
        if (qtdeEstoque == null) {
            if (other.qtdeEstoque != null)
                return false;
        } else if (!qtdeEstoque.equals(other.qtdeEstoque))
            return false;
        return true;
    }


}
