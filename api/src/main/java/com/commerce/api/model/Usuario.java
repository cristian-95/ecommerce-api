package com.commerce.api.model;

import com.commerce.api.model.dto.ClienteDTO;
import com.commerce.api.model.dto.LojaDTO;
import com.commerce.api.validation.constraints.Telefone;
import com.commerce.api.validation.constraints.Username;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "Usuario_Type")
@Table(name = "usuarios")
@JsonIgnoreProperties({"authorities"})
public class Usuario extends RepresentationModel<Usuario> implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    @Username
    private String username;
    @Email(message = "E-mail em formato inválido.")
    private String email;
    @JsonIgnore
    private String password;
    private String nome;
    @Column(nullable = true)
    @Telefone
    private String telefone;
    private String endereco;

    @JsonIgnore
    private UserRole role;
    @JsonIgnore
    private Boolean enabled;
    @JsonIgnore
    private Boolean accountNonLocked;
    @JsonIgnore
    private Boolean accountNonExpired;
    @JsonIgnore
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
        setAccountInitialProperties();
    }

    public Usuario(LojaDTO dto) {
        this.username = dto.username();
        this.email = dto.email();
        this.password = dto.password();
        this.nome = dto.nome();
        this.telefone = dto.telefone();
        this.endereco = dto.endereco();
        setAccountInitialProperties();
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
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        if (!super.equals(o)) return false;

        return id.equals(usuario.id) && username.equals(usuario.username);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }
}
