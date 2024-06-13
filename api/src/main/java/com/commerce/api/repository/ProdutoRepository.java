package com.commerce.api.repository;

import com.commerce.api.model.Produto;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Hidden
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByLojaId(Long lojaId);

    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(p.descricao) LIKE LOWER(CONCAT('%', ?2, '%'))")
    public Page<Produto> findByNomeOrDescricao(String nome, String descricao, Pageable pageable);


}