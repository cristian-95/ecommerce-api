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
}
