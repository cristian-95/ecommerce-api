package com.commerce.api.repository;

import com.commerce.api.model.Cliente;
import com.commerce.api.model.Loja;
import com.commerce.api.model.Pedido;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Hidden
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByCliente(Cliente cliente);

    List<Pedido> findAllByLoja(Loja loja);

    Pedido findByCodigo(String codigo);

}