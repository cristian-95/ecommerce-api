package com.commerce.api.service;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.*;
import com.commerce.api.model.dto.PedidoDTO;
import com.commerce.api.model.dto.PedidoUpdateDTO;
import com.commerce.api.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;
    @Autowired
    private CarrinhoDeComprasService carrinhoDeComprasService;
    @Autowired
    private LojaService lojaService;
    @Autowired
    private ClienteService clienteService;


    public List<Pedido> getAllPedidos() {
        return this.repository.findAll();
    }

    public Pedido getPedidoById(Long id) throws ResourceNotFoundException {
        return this.repository.findById(id).orElseThrow();
    }

    public Pedido createPedido(PedidoDTO dto) throws ResourceNotFoundException {
        CarrinhoDeCompras carrinho = this.carrinhoDeComprasService.getCarrinhoDeComprasById(dto.carrinhoId());
        Loja loja = this.lojaService.getLojaById(dto.lojaId());
        Cliente cliente = this.clienteService.getClienteById(dto.clienteId());
        cliente.novoCarrinho();
        Pedido pedido = new Pedido(cliente, loja, PedidoStatus.PENDENTE, carrinho);
        this.carrinhoDeComprasService.save(carrinho);
        this.clienteService.save(cliente);
        return this.repository.save(pedido);
    }

    public Pedido updatePedido(Long id, PedidoUpdateDTO dto) throws IllegalArgumentException {
        Pedido pedido = getPedidoById(id);
        PedidoStatus status = PedidoStatus.valueOf(dto.status().toUpperCase());
        pedido.setStatus(status);
        return this.repository.save(pedido);
    }

    public void deletePedido(Long id) {
        try {
            Pedido pedido = this.repository.findById(id).get();
            this.repository.delete(pedido);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Pedido (id = %d) não encontrado".formatted(id));
        }
    }
}
