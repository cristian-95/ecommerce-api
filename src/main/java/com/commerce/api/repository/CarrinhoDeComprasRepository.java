package com.commerce.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commerce.api.model.CarrinhoDeCompras;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository
public interface CarrinhoDeComprasRepository extends JpaRepository<CarrinhoDeCompras, Long> {
}