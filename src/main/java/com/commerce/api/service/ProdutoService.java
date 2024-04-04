package com.commerce.api.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.ProdutoDTO;
import com.commerce.api.repository.ProdutoRepository;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository repository;

    public List<Produto> getAllProdutos() {
        return repository.findAll();
    }

    public Produto getProdutoById(Long id) throws ResourceNotFoundException {
        try {
            Produto produto = repository.findById(id).get();
            return produto;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Produto (id = %d) não encontrado".formatted(id));
        }
    }

    public Produto createProduto(ProdutoDTO dto) {
        var produto = new Produto(dto);
        return repository.save(produto);
    }

    public Produto updateProduto(ProdutoDTO dto) throws ResourceNotFoundException {
        Produto produto = getProdutoById(dto.id());

        produto.setNome(dto.nome() != null ? dto.nome() : produto.getNome());
        produto.setDescricao(dto.descricao() != null ? dto.descricao() : produto.getDescricao());
        produto.setPreco(dto.preco() != null ? dto.preco() : produto.getPreco());
        produto.setSpecs(dto.specs() != null ? dto.specs() : produto.getSpecs());
        produto.setQtdeEstoque(dto.qtdeEstoque() != null ? dto.qtdeEstoque() : produto.getQtdeEstoque());

        return repository.save(produto);
    }

    public void deleteProduto(Long id) throws Exception {        
        try {
            Produto produto = repository.findById(id).get();
            repository.delete(produto);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Produto (id = %d) não encontrado".formatted(id));
        }
    }
}
