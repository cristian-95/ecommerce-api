package com.commerce.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commerce.api.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto,Long> {
    
}