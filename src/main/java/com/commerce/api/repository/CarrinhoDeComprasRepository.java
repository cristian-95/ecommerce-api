package com.commerce.api.repository;

import com.commerce.api.model.CarrinhoDeCompras;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface CarrinhoDeComprasRepository extends JpaRepository<CarrinhoDeCompras, Long> {
}