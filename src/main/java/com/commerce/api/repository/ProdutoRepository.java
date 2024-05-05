package com.commerce.api.repository;

import com.commerce.api.model.Produto;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Hidden
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByLojaId(Long lojaId);

}