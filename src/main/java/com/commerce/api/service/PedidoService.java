package com.commerce.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.CarrinhoDeCompras;
import com.commerce.api.model.Cliente;
import com.commerce.api.model.Loja;
import com.commerce.api.model.Pedido;
import com.commerce.api.model.PedidoStatus;
import com.commerce.api.model.dto.PedidoDTO;
import com.commerce.api.repository.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;
    @Autowired
    private CarrinhoService carrinhoService;
    @Autowired
    private LojaService lojaService;
    @Autowired
    private ClienteService clienteService;
    

    public List<Pedido> getAllPedidos() {
        return repository.findAll();
    }

    public Pedido getPedidoById(Long id) throws ResourceNotFoundException {
        try {
            Pedido pedido = repository.findById(id).get();
            return pedido;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Pedido (id = %d) não encontrado".formatted(id));
        }
    }

    public Pedido createPedido(PedidoDTO dto) throws ResourceNotFoundException {
        CarrinhoDeCompras carrinho = carrinhoService.getCarrinhoDeComprasById(dto.carrinhoId());
        Loja loja  = lojaService.getLojaById(dto.lojaId());
        Cliente cliente = clienteService.getClienteById(dto.clienteId());
        Pedido pedido = new Pedido(cliente, loja, PedidoStatus.PENDENTE, carrinho);
        return repository.save(pedido);
    }

    public void deletePedido(Long id) throws Exception {
        try {
            Pedido pedido = repository.findById(id).get();
            repository.delete(pedido);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Pedido (id = %d) não encontrado".formatted(id));
        }
    }
}
