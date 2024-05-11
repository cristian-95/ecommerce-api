package com.commerce.api.repository;

import com.commerce.api.model.Cliente;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByUsername(String username);

    Boolean existsByUsername(String username);

}
