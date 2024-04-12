package com.commerce.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Cliente;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.ClienteDTO;
import com.commerce.api.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;
    @Autowired
    private ProdutoService produtoService;

    public List<Cliente> getAllClientes() {

        return repository.findAll();
    }

    public Cliente getClienteById(Long id) {
        try {
            Cliente cliente = (Cliente) repository.findById(id).get();
            return cliente;
        } catch (Exception e) {
            return null;
        }
    }

    public Cliente createCliente(ClienteDTO dto) {
        Cliente cliente = new Cliente(dto);
        return repository.save(cliente);
    }

    public Cliente updateCliente(ClienteDTO dto) {
        Cliente cliente = getClienteById(dto.id());
        cliente = update(cliente, dto);
        return repository.save(cliente);
    }

    public void deleteCliente(Long id) throws Exception {
        try {
            Cliente cliente = (Cliente) repository.findById(id).get();
            repository.delete(cliente);
        } catch (Exception e) {
            System.err.println("DELETE:Cliente: %d não encontrado".formatted(id));
        }
    }

    public Produto addFavorito(Long clienteId, Long produtoId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        Produto produto = produtoService.getProdutoById(produtoId);
        cliente.adicionarFavorito(produto);
        repository.save(cliente);
        return produto;
    }

    public Produto removeFavorito(Long clienteId, Long produtoId) throws ResourceNotFoundException {
        Cliente cliente = getClienteById(clienteId);
        Produto produto = produtoService.getProdutoById(produtoId);
        cliente.removerFavorito(produto);
        repository.save(cliente);
        return produto;
    }

    public List<Produto> getAllFavoritos(Long clienteId) {
        Cliente cliente = getClienteById(clienteId);
        return cliente.getFavoritos();
    }

    private Cliente update(Cliente cliente, ClienteDTO dto) {
        cliente.setNome(dto.nome() != null ? dto.nome() : cliente.getNome());
        cliente.setSobrenome(dto.sobrenome() != null ? dto.sobrenome() : cliente.getSobrenome());
        cliente.setCPF(dto.CPF() != null ? dto.CPF() : cliente.getCPF());
        cliente.setEmail(dto.email() != null ? dto.email() : cliente.getEmail());
        cliente.setTelefone(dto.telefone() != null ? dto.telefone() : cliente.getTelefone());
        cliente.setEndereco(dto.endereco() != null ? dto.endereco() : cliente.getEndereco());
        cliente.setDataNasc(dto.dataNasc() != null ? dto.dataNasc() : cliente.getDataNasc());
        cliente.setGenero(dto.genero() != null ? dto.genero() : cliente.getGenero());
        return cliente;
    }
}
