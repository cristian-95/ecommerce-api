package com.commerce.api.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.commerce.api.model.dto.ClienteDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
@DiscriminatorValue("Cliente")
public class Cliente extends Usuario {

    private static final long serialVersionUID = 1L;

    private String sobrenome;
    private String CPF;
    private LocalDate dataNasc;
    private String genero;
    @ManyToMany
    @JoinTable(name = "favoritos", joinColumns = @JoinColumn(name = "cliente_id"), inverseJoinColumns = @JoinColumn(name = "produto_id"))
    private List<Produto> favoritos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "carrinho_id", referencedColumnName = "id")
    private CarrinhoDeCompras carrinhoDeCompras;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;

    public Cliente() {
        super();
        this.favoritos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.carrinhoDeCompras = new CarrinhoDeCompras();
    }

    public Cliente(ClienteDTO dto) {
        super(dto);
        this.sobrenome = dto.sobrenome();
        this.CPF = dto.CPF();
        this.dataNasc = dto.dataNasc();
        this.genero = dto.genero();
        this.favoritos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.carrinhoDeCompras = new CarrinhoDeCompras();
    }

    public Cliente(String username, String encriptedPassword, String role) {
        super(username, encriptedPassword, role);
        this.favoritos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.carrinhoDeCompras = new CarrinhoDeCompras();
    }

    public Cliente(String username, String email, String password, String nome, String telefone, String endereco,
            UserRole role, String sobrenome, String cPF, LocalDate dataNasc, String genero) {
        super(username, email, password, nome, telefone, endereco, role);
        this.sobrenome = sobrenome;
        CPF = cPF;
        this.dataNasc = dataNasc;
        this.genero = genero;
        this.favoritos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.carrinhoDeCompras = new CarrinhoDeCompras();
    }

    public Cliente(String username, String email, String password, String nome, String telefone, String endereco,
            UserRole role, String sobrenome, String cPF, LocalDate dataNasc, String genero, List<Produto> favoritos,
            CarrinhoDeCompras carrinhoDeCompras, List<Pedido> pedidos) {
        super(username, email, password, nome, telefone, endereco, role);
        this.sobrenome = sobrenome;
        CPF = cPF;
        this.dataNasc = dataNasc;
        this.genero = genero;
        this.favoritos = favoritos;
        this.carrinhoDeCompras = carrinhoDeCompras;
        this.pedidos = pedidos;
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

    public void adicionarAoCarrinho(Produto produto) {
        this.carrinhoDeCompras.adicionarItem(produto);
    }

    public void removerDoCarrinho(Produto produto) {
        this.carrinhoDeCompras.removerItem(produto);
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

    public CarrinhoDeCompras getCarrinhoDeCompras() {
        return this.carrinhoDeCompras;
    }

    public void setCarrinhoDeCompras(CarrinhoDeCompras carrinhoDeCompras) {
        this.carrinhoDeCompras = carrinhoDeCompras;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((sobrenome == null) ? 0 : sobrenome.hashCode());
        result = prime * result + ((CPF == null) ? 0 : CPF.hashCode());
        result = prime * result + ((dataNasc == null) ? 0 : dataNasc.hashCode());
        result = prime * result + ((genero == null) ? 0 : genero.hashCode());
        result = prime * result + ((favoritos == null) ? 0 : favoritos.hashCode());
        result = prime * result + ((carrinhoDeCompras == null) ? 0 : carrinhoDeCompras.hashCode());
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
        Cliente other = (Cliente) obj;
        if (sobrenome == null) {
            if (other.sobrenome != null)
                return false;
        } else if (!sobrenome.equals(other.sobrenome))
            return false;
        if (CPF == null) {
            if (other.CPF != null)
                return false;
        } else if (!CPF.equals(other.CPF))
            return false;
        if (dataNasc == null) {
            if (other.dataNasc != null)
                return false;
        } else if (!dataNasc.equals(other.dataNasc))
            return false;
        if (genero == null) {
            if (other.genero != null)
                return false;
        } else if (!genero.equals(other.genero))
            return false;
        if (favoritos == null) {
            if (other.favoritos != null)
                return false;
        } else if (!favoritos.equals(other.favoritos))
            return false;
        if (carrinhoDeCompras == null) {
            if (other.carrinhoDeCompras != null)
                return false;
        } else if (!carrinhoDeCompras.equals(other.carrinhoDeCompras))
            return false;
        if (pedidos == null) {
            if (other.pedidos != null)
                return false;
        } else if (!pedidos.equals(other.pedidos))
            return false;
        return true;
    }

}
