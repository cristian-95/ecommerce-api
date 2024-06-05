package com.commerce.api.service;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.*;
import com.commerce.api.model.dto.PedidoUpdateDTO;
import com.commerce.api.model.dto.RequestDTO;
import com.commerce.api.repository.ItemRepository;
import com.commerce.api.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private CarrinhoDeComprasService carrinhoDeComprasService;
    @Autowired
    private LojaService lojaService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ItemRepository itemRepository;


    public List<Pedido> getAllPedidos(String username) {
        List<Pedido> pedidos;
        if (clienteService.getProfile(username) != null) {
            Cliente cliente = clienteService.getProfile(username);
            pedidos = this.pedidoRepository.findAllByCliente(cliente);
        } else {
            Loja loja = lojaService.getProfile(username);
            pedidos = this.pedidoRepository.findAllByLoja(loja);
        }
        Collections.reverse(pedidos);
        return pedidos;
    }

    public Pedido getPedido(String username) {
        if (clienteService.getProfile(username) != null) {
            Cliente cliente = clienteService.getProfile(username);
            return this.pedidoRepository.findByCliente(cliente);
        } else {
            Loja loja = lojaService.getProfile(username);
            return this.pedidoRepository.findByLoja(loja);
        }
    }

    public Pedido getPedidoByCodigo(String codigo) {
        return pedidoRepository.findByCodigo(codigo);
    }

    public Pedido getPedidoById(Long id) throws ResourceNotFoundException {
        return this.pedidoRepository.findById(id).orElseThrow();
    }

    public List<Pedido> processarPedidos(String username) throws ResourceNotFoundException {

        Cliente cliente = this.clienteService.getProfile(username);
        CarrinhoDeCompras carrinhoDeCompras = cliente.getCarrinhoDeCompras().get(0);
        Map<Loja, List<Item>> itensPorLoja = agruparItensPorLoja(carrinhoDeCompras.getItens());

        List<Pedido> pedidos = new ArrayList<>();
        for (Map.Entry<Loja, List<Item>> entry : itensPorLoja.entrySet()) {
            Loja loja = entry.getKey();
            List<Item> itens = entry.getValue();

            Pedido pedido = new Pedido(cliente, loja, itens);
            salvarPedido(pedido, cliente, loja);
            pedidos.add(pedido);
        }
        CarrinhoDeCompras novoCarrinho = new CarrinhoDeCompras(cliente);
        criarCarrinhoNovo(carrinhoDeCompras, novoCarrinho, cliente);
//        removerCarrinhoAntigo(carrinhoDeCompras);

        this.clienteService.save(cliente);
        return pedidos;
    }

    public Pedido updatePedido(String username, PedidoUpdateDTO dto) throws IllegalArgumentException {
        Loja loja = lojaService.getProfile(username);
        Pedido pedido = getPedidoByCodigo(dto.codigo());
        if (!loja.equals(pedido.getLoja())) {
            return null;
        }
        PedidoStatus status = PedidoStatus.valueOf(dto.status().toUpperCase());
        pedido.setStatus(status);
        return this.pedidoRepository.save(pedido);
    }

    public void deletePedido(String username, RequestDTO requestDTO) {
        try {
            Loja loja = lojaService.getProfile(username);
            Pedido pedido = this.pedidoRepository.findById(requestDTO.id()).get();
            if (loja.equals(pedido.getLoja()))
                this.pedidoRepository.delete(pedido);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Pedido (id = %d) não encontrado".formatted(requestDTO.id()));
        }
    }

    private Map<Loja, List<Item>> agruparItensPorLoja(List<Item> itens) {
        return itens.stream()
                .collect(Collectors.groupingBy(i -> i.getProduto().getLoja()));
    }

    private void salvarPedido(Pedido pedido, Cliente cliente, Loja loja) {
        cliente.adicionarPedido(pedido);
        loja.adicionarPedido(pedido);

        this.pedidoRepository.save(pedido);
        List<Item> itens = pedido.getItens();
        itens.forEach(i -> {
            Item item = itemRepository.findById(i.getId()).get();
            item.setPedido(pedido);
            itemRepository.save(item);
        });

        this.lojaService.save(loja);
    }

    private void removerCarrinhoAntigo(CarrinhoDeCompras carrinho) {
        this.carrinhoDeComprasService.remover(carrinho);
    }

    private void criarCarrinhoNovo(CarrinhoDeCompras carrinhoDeCompras, CarrinhoDeCompras novoCarrinho, Cliente cliente) {
        this.carrinhoDeComprasService.create(novoCarrinho);
        cliente.getCarrinhoDeCompras().add(novoCarrinho);
        cliente.getCarrinhoDeCompras().remove(carrinhoDeCompras);
    }
}
