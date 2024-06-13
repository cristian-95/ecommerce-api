package com.commerce.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "imagens")
@JsonPropertyOrder({"nome", "tipo", "path"})
public class Imagem extends RepresentationModel<Imagem> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String tipo;
    private String path;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    @JsonBackReference
    private Produto produto;

    public Imagem() {
    }

    public Imagem(String nome, String tipo, String path) {
        this.nome = nome;
        this.tipo = tipo;
        this.path = path;
    }

    public Imagem(String nome, String tipo, String path, Produto produto) {
        this.nome = nome;
        this.tipo = tipo;
        this.path = path;
        this.produto = produto;
    }

    public Imagem(Long id, String nome, String tipo, String path, Produto produto) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.path = path;
        this.produto = produto;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
