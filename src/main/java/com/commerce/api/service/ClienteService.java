package com.commerce.api.service;

import com.commerce.api.controller.ClienteController;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.CarrinhoDeCompras;
import com.commerce.api.model.Cliente;
import com.commerce.api.model.Pedido;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.ClienteDTO;
import com.commerce.api.model.dto.ClienteUpdateDTO;
import com.commerce.api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;
    @Autowired
    private ProdutoService produtoService;

    public List<Cliente> getAllClientes() {
        List<Cliente> clientes = repository.findAll();
        clientes.forEach(c -> c.add(linkTo(methodOn(ClienteController.class).getById(c.getId())).withSelfRel()));
        return clientes;
    }

    public Cliente getClienteById(Long id) throws ResourceNotFoundException {
        Cliente cliente = (Cliente) repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        cliente.add(linkTo(methodOn(ClienteController.class).getAll()).withRel("Listagem"));
        return cliente;
    }

    public Cliente createCliente(ClienteDTO dto) throws ResourceNotFoundException {
        Cliente cliente = new Cliente(dto);
        String encriptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        cliente.setPassword(encriptedPassword);
        Cliente saved = repository.save(cliente);
        saved.add(linkTo(methodOn(ClienteController.class).getById(saved.getId())).withSelfRel());
        return saved;
    }

    public Cliente updateCliente(ClienteUpdateDTO dto) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(dto.id());
        Cliente updated = repository.save(updateProperties(cliente, dto));
        updated.add(linkTo(methodOn(ClienteController.class).getById(updated.getId())).withSelfRel());
        return updated;
    }

    public void deleteCliente(Long id) throws Exception {
        try {
            Cliente cliente = (Cliente) repository.findById(id).get();
            repository.delete(cliente);
        } catch (Exception e) {
            System.err.println("DELETE:Cliente: %d não encontrado".formatted(id));
        }
    }

    public CarrinhoDeCompras getCarrinho(Long clienteId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        return cliente.getCarrinhoDeCompras();
    }

    public Produto adicionarAoCarrinho(Long clienteId, Long produtoId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        Produto produto = produtoService.getProdutoById(produtoId);
        cliente.adicionarAoCarrinho(produto);
        ;
        repository.save(cliente);
        return produto;
    }

    public Produto removerDoCarrinho(Long clienteId, Long produtoId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        Produto produto = produtoService.getProdutoById(produtoId);
        cliente.removerDoCarrinho(produto);
        repository.save(cliente);
        return produto;
    }

    public Produto adicionarFavorito(Long clienteId, Long produtoId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        Produto produto = produtoService.getProdutoById(produtoId);
        cliente.adicionarFavorito(produto);
        repository.save(cliente);
        return produto;
    }

    public Produto removerFavorito(Long clienteId, Long produtoId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        Produto produto = produtoService.getProdutoById(produtoId);
        cliente.removerFavorito(produto);
        repository.save(cliente);
        return produto;
    }

    public List<Produto> getAllFavoritos(Long clienteId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        return cliente.getFavoritos();
    }

    public List<Pedido> getAllPedidos(Long clienteId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        return cliente.getPedidos();
    }

    private Cliente updateProperties(Cliente cliente, ClienteUpdateDTO dto) {
        cliente.setNome(dto.nome() != null ? dto.nome() : cliente.getNome());
        cliente.setSobrenome(dto.sobrenome() != null ? dto.sobrenome() : cliente.getSobrenome());
        cliente.setCpf(dto.CPF() != null ? dto.CPF() : cliente.getCpf());
        cliente.setEmail(dto.email() != null ? dto.email() : cliente.getEmail());
        cliente.setTelefone(dto.telefone() != null ? dto.telefone() : cliente.getTelefone());
        cliente.setEndereco(dto.endereco() != null ? dto.endereco() : cliente.getEndereco());
        cliente.setDataNasc(dto.dataNasc() != null ? dto.dataNasc() : cliente.getDataNasc());
        cliente.setGenero(dto.genero() != null ? dto.genero() : cliente.getGenero());
        return cliente;
    }
}
