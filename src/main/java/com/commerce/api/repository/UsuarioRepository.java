package com.commerce.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commerce.api.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);
}
