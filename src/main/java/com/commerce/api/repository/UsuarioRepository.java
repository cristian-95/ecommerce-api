package com.commerce.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commerce.api.model.Usuario;

@Repository
public interface UsuarioRepository<T extends Usuario> extends JpaRepository<Usuario, Long> {
}
