package com.commerce.api.model;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table
@DiscriminatorValue("Admin")
public class Admin extends Usuario {

    public Admin() {
        super();
    }

    public Admin(String username, String email, String password, String nome, String telefone, String endereco, UserRole role) {
        super(username, email, password, nome, telefone, endereco, role);
    }

    public Admin(String username, String password, UserRole role) {
        super(username, password, role);
    }

    public Admin(String username, String encryptedPassword, String role) {
        super(username, encryptedPassword, role);
    }
}
