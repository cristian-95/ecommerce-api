package com.commerce.api.repository;

import com.commerce.api.model.Loja;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface LojaRepository extends JpaRepository<Loja, Long> {

    Loja findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query("SELECT l FROM Loja l WHERE LOWER(l.nome) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(l.descricao) LIKE LOWER(CONCAT('%', ?2, '%'))")
    public Page<Loja> findByNomeOrDescricao(String nome, String descricao, Pageable pageable);
}
