package com.commerce.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commerce.api.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
