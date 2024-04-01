package com.commerce.api.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.commerce.api.model.dto.UsuarioDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    private String nome;
    private String sobrenome;
    private String documento;
    private String email;
    private String endereco;
    private LocalDate dataNasc;
    private String genero;
    @ManyToMany
    @JoinTable(name = "favoritos", joinColumns = @JoinColumn(name = "usuarios_id"), inverseJoinColumns = @JoinColumn(name = "produtos_id"))
    private List<Produto> favoritos;

    private UserRole role;
    private Boolean enabled;
    private Boolean accountNonLocked;
    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired;

    public Usuario() {
        this.favoritos = new ArrayList<>();
        setAccountInitialProperties();
    }

    public Usuario(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.favoritos = new ArrayList<>();
        setAccountInitialProperties();
    }

    public Usuario(String username, String encriptedPassword, String role) {
        this.username = username;
        this.password = encriptedPassword;
        this.role = UserRole.valueOf(role.toUpperCase());
        this.favoritos = new ArrayList<>();
        setAccountInitialProperties();
    }

    // public Usuario(Long id, String username, String password, String nome, String
    // sobrenome, String documento,
    // String email, String endereco, LocalDate dataNasc, String genero,
    // List<Produto> favoritos) {
    // this.id = id;
    // this.username = username;
    // this.password = password;
    // this.nome = nome;
    // this.sobrenome = sobrenome;
    // this.documento = documento;
    // this.email = email;
    // this.endereco = endereco;
    // this.dataNasc = dataNasc;
    // this.genero = genero;
    // this.favoritos = favoritos;
    // this.enabled = true;
    // this.accountNonLocked = true;
    // this.accountNonExpired = true;
    // this.credentialsNonExpired = true;
    // }

    public Usuario(Long id, String username, String password, String nome, String sobrenome, String documento,
            String email, String endereco, LocalDate dataNasc, String genero, List<Produto> favoritos, UserRole role,
            Boolean enabled, Boolean accountNonLocked, Boolean accountNonExpired, Boolean credentialsNonExpired) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.documento = documento;
        this.email = email;
        this.endereco = endereco;
        this.dataNasc = dataNasc;
        this.genero = genero;
        this.favoritos = favoritos;
        this.role = role;
        this.enabled = enabled;
        this.accountNonLocked = accountNonLocked;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Usuario(UsuarioDTO dto) {
        this.id = dto.id();
        this.username = dto.username();
        this.password = dto.password();
        this.nome = dto.nome();
        this.sobrenome = dto.sobrenome();
        this.documento = dto.documento();
        this.email = dto.email();
        this.endereco = dto.endereco();
        this.dataNasc = dto.dataNasc();
        this.genero = dto.genero();
        this.favoritos = new ArrayList<>();
        this.role = UserRole.valueOf(dto.role().toUpperCase());
        setAccountInitialProperties();
    }

    public void addFavorito(Produto produto) {
        if (!this.favoritos.contains(produto)) {
            this.favoritos.add(produto);
        }
    }

    public void removeFavorito(Produto produto) {
        if (!this.favoritos.contains(produto)) {
            this.favoritos.remove(produto);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (this.getRole()) {
            case "manager" -> List.of(
                    new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_MANAGER"));
            case "admin" -> List.of(
                    new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_MANAGER"),
                    new SimpleGrantedAuthority("ROLE_ADMIN"));
            default -> List.of(new SimpleGrantedAuthority("ROLE_USER"));
        };
    }

    public String getRole() {
        return role.getRole();
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((sobrenome == null) ? 0 : sobrenome.hashCode());
        result = prime * result + ((documento == null) ? 0 : documento.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
        result = prime * result + ((dataNasc == null) ? 0 : dataNasc.hashCode());
        result = prime * result + ((genero == null) ? 0 : genero.hashCode());
        result = prime * result + ((favoritos == null) ? 0 : favoritos.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
        result = prime * result + ((accountNonLocked == null) ? 0 : accountNonLocked.hashCode());
        result = prime * result + ((accountNonExpired == null) ? 0 : accountNonExpired.hashCode());
        result = prime * result + ((credentialsNonExpired == null) ? 0 : credentialsNonExpired.hashCode());
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
        Usuario other = (Usuario) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (sobrenome == null) {
            if (other.sobrenome != null)
                return false;
        } else if (!sobrenome.equals(other.sobrenome))
            return false;
        if (documento == null) {
            if (other.documento != null)
                return false;
        } else if (!documento.equals(other.documento))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (endereco == null) {
            if (other.endereco != null)
                return false;
        } else if (!endereco.equals(other.endereco))
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
        if (role != other.role)
            return false;
        if (enabled == null) {
            if (other.enabled != null)
                return false;
        } else if (!enabled.equals(other.enabled))
            return false;
        if (accountNonLocked == null) {
            if (other.accountNonLocked != null)
                return false;
        } else if (!accountNonLocked.equals(other.accountNonLocked))
            return false;
        if (accountNonExpired == null) {
            if (other.accountNonExpired != null)
                return false;
        } else if (!accountNonExpired.equals(other.accountNonExpired))
            return false;
        if (credentialsNonExpired == null) {
            if (other.credentialsNonExpired != null)
                return false;
        } else if (!credentialsNonExpired.equals(other.credentialsNonExpired))
            return false;
        return true;
    }

    private void setAccountInitialProperties() {
        this.enabled = true;
        this.accountNonLocked = true;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
    }
}
