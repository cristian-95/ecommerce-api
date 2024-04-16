package com.commerce.api.model;

import com.commerce.api.model.dto.ClienteDTO;
import com.commerce.api.model.dto.LojaDTO;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "Usuario_Type")
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private String nome;
    private String telefone;
    private String endereco;

    private UserRole role;
    private Boolean enabled;
    private Boolean accountNonLocked;
    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired;

    public Usuario() {
        setAccountInitialProperties();
    }

    public Usuario(String username, String email, String password, String nome, String telefone, String endereco,
            UserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
        this.role = role;
        setAccountInitialProperties();
    }

    public Usuario(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
        setAccountInitialProperties();
    }

    public Usuario(String username, String encriptedPassword, String role) {
        this.username = username;
        this.password = encriptedPassword;
        this.role = UserRole.valueOf(role.toUpperCase());
        setAccountInitialProperties();
    }

    public Usuario(ClienteDTO dto) {
        this.username = dto.username();
        this.email = dto.email();
        this.password = dto.password();
        this.nome = dto.nome();
        this.telefone = dto.telefone();
        this.endereco = dto.endereco();
    }

    public Usuario(LojaDTO dto) {
        this.username = dto.username();
        this.email = dto.email();
        this.password = dto.password();
        this.nome = dto.nome();
        this.telefone = dto.telefone();
        this.endereco = dto.endereco();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (this.getRole().toUpperCase()) {
            case "MANAGER" -> List.of(
                    new SimpleGrantedAuthority("ROLE_MANAGER"));
            case "ADMIN" -> List.of(
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

    private void setAccountInitialProperties() {
        this.enabled = true;
        this.accountNonLocked = true;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
        result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
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
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
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
        if (telefone == null) {
            if (other.telefone != null)
                return false;
        } else if (!telefone.equals(other.telefone))
            return false;
        if (endereco == null) {
            if (other.endereco != null)
                return false;
        } else if (!endereco.equals(other.endereco))
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
}
