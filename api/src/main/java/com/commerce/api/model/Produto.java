package com.commerce.api.model;

import com.commerce.api.model.dto.ProdutoDTO;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "produtos")
@JsonPropertyOrder({"id", "nome", "descricao", "preco", "specs", "qtdeEstoque","imagens"})
@Relation(collectionRelation = "produtos")
public class Produto extends RepresentationModel<Produto> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 2, max = 80)
    private String nome;
    @Column(length = 1000)
    private String descricao;
    private HashMap<String, String> specs;
    private Double preco;
    @Min(0)
    private Integer qtdeEstoque;

    @JsonIgnore
    @ManyToMany(mappedBy = "favoritos")
    private List<Cliente> clientes;

    @JsonIgnore
    @OneToMany(mappedBy = "produto", cascade = CascadeType.REMOVE)
    private List<Item> itens;

    @JsonIncludeProperties({"id", "nome"})
    @ManyToOne
    @JoinColumn(name = "loja_id")
    private Loja loja;

    @JsonManagedReference
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<Imagem> imagens;

    public Produto() {
        this.imagens = new ArrayList<>();
    }

    public Produto(Long id, String nome, HashMap<String, String> specs, Double preco, Integer qtdeEstoque,
                   String descricao) {
        this.id = id;
        this.nome = nome;
        this.specs = specs;
        this.descricao = descricao;
        this.preco = preco;
        this.qtdeEstoque = qtdeEstoque;
        this.imagens = new ArrayList<>();
    }

    public Produto(ProdutoDTO dto) {
        this.nome = dto.nome();
        this.specs = dto.specs();
        this.descricao = dto.descricao();
        this.preco = dto.preco();
        this.qtdeEstoque = dto.qtdeEstoque();
        this.imagens = new ArrayList<>();
    }

    public void adicionarImagem(Imagem imagem) {
        this.imagens.add(imagem);
    }

    public void removerImagem(Imagem imagem) {
        this.imagens.remove(imagem);
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

    public Integer getQtdeEstoque() {
        return qtdeEstoque;
    }

    public void setQtdeEstoque(Integer qtdeEstoque) {
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

    public List<Imagem> getImagens() {
        return imagens;
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
            return other.qtdeEstoque == null;
        } else return qtdeEstoque.equals(other.qtdeEstoque);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", specs=" + specs +
                ", preco=" + preco +
                ", qtdeEstoque=" + qtdeEstoque +
                ", clientes=" + clientes +
                ", itens=" + itens +
                ", loja=" + loja +
                '}';
    }
}
