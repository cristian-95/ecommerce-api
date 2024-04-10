package com.commerce.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Loja;
import com.commerce.api.model.Produto;
import com.commerce.api.model.Usuario;
import com.commerce.api.model.dto.LojaDTO;

import com.commerce.api.repository.UsuarioRepository;

@Service
public class LojaService {

    @Autowired
    private UsuarioRepository<Loja> repository;
    @Autowired
    private ProdutoService produtoService;

    public List<Usuario> getAllLojas() {
        return repository.findAll();
    }

    public Loja getLojaById(Long id) {
        try {
            Loja loja = (Loja) repository.findById(id).get();
            return loja;
        } catch (Exception e) {
            return null;
        }
    }

    public Loja createLoja(LojaDTO dto) {
        Loja loja = new Loja(dto);
        return repository.save(loja);
    }

    public Loja updateLoja(LojaDTO dto) {
        Loja loja = getLojaById(dto.id());
        loja = update(loja, dto);
        return repository.save(loja);
    }

    public void deleteLoja(Long id) throws Exception {
        try {
            Loja loja = (Loja) repository.findById(id).get();
            repository.delete(loja);
        } catch (Exception e) {
            System.err.println("DELETE:Loja: %d não encontrado".formatted(id));
        }
    }

    public void addProduto(Long lojaId, Long produtoId) throws ResourceNotFoundException {
        Loja loja = getLojaById(lojaId);
        Produto produto = produtoService.getProdutoById(produtoId);
        
        produto.setLoja(loja);
        loja.addProduto(produto);
        
        produtoService.updateProduto(produto);
        repository.save(loja);
        
    }

    public void removeProduto(Long lojaId, Long produtoId) throws ResourceNotFoundException {
        Loja loja = getLojaById(lojaId);
        Produto produto = produtoService.getProdutoById(produtoId);
        
        produto.setLoja(null);
        loja.removeProduto(produto);
        
        produtoService.updateProduto(produto);
        repository.save(loja);
        
    }

    public List<Produto> getAllProdutos(Long lojaId) {
        Loja loja = getLojaById(lojaId);
        return loja.getProdutos();
    }

    private Loja update(Loja loja, LojaDTO dto) {
        loja.setNome(dto.nome() != null ? dto.nome() : loja.getNome());        
        loja.setCNPJ(dto.CNPJ() != null ? dto.CNPJ() : loja.getCNPJ());
        loja.setEmail(dto.email() != null ? dto.email() : loja.getEmail());
        loja.setTelefone(dto.telefone() != null ? dto.telefone() : loja.getTelefone());
        loja.setEndereco(dto.endereco() != null ? dto.endereco() : loja.getEndereco());        
        return loja;
    }
}
