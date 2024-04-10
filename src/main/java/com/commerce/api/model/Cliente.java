package com.commerce.api.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.commerce.api.model.dto.ClienteDTO;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
@DiscriminatorValue("Cliente")
public class Cliente extends Usuario {

    private String sobrenome;
    private String CPF;
    private LocalDate dataNasc;
    private String genero;
    @ManyToMany
    @JoinTable(name = "favoritos", joinColumns = @JoinColumn(name = "cliente_id"), inverseJoinColumns = @JoinColumn(name = "produto_id"))
    private List<Produto> favoritos;

    public Cliente() {
        this.favoritos = new ArrayList<>();
    }

    public Cliente(ClienteDTO dto) {
        super(dto);        
        this.sobrenome = dto.sobrenome();
        this.CPF = dto.CPF();
        this.dataNasc = dto.dataNasc();
        this.genero = dto.genero();
        this.favoritos = new ArrayList<>();
    }

    public void adicionarFavorito(Produto produto) {
        if (!this.favoritos.contains(produto)) {
            this.favoritos.add(produto);
        }
    }

    public void removerFavorito(Produto produto) {
        if (!this.favoritos.contains(produto)) {
            this.favoritos.remove(produto);
        }
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public LocalDate getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(LocalDate dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public List<Produto> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<Produto> favoritos) {
        this.favoritos = favoritos;
    }    
}
