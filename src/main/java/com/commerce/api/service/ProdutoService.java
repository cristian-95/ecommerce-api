package com.commerce.api.service;

import com.commerce.api.controller.ProdutoController;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.ProdutoDTO;
import com.commerce.api.repository.ProdutoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> getAllProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        produtos.forEach(p -> p.add(linkTo(methodOn(ProdutoController.class).getById(p.getId())).withSelfRel()));
        return produtos;
    }

    public Produto getProdutoById(Long id) throws ResourceNotFoundException {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        produto.add(linkTo(methodOn(ProdutoController.class).getAll()).withRel("Listagem"));
        return produto;
    }

    public Produto createProduto(ProdutoDTO dto) {
        Produto produto = new Produto(dto);
        Produto saved = produtoRepository.save(produto);
        saved.add(linkTo(methodOn(ProdutoController.class).getById(saved.getId())).withSelfRel());
        return saved;
    }

    public Produto updateProduto(ProdutoDTO dto) throws ResourceNotFoundException {
        Produto produto = getProdutoById(dto.id());
        Produto updated = produtoRepository.save(updateProperties(produto, dto));
        updated.add(linkTo(methodOn(ProdutoController.class).getById(updated.getId())).withSelfRel());
        return updated;
    }

    public void updateProduto(Produto updatedProduto) throws ResourceNotFoundException {
        Produto produto = getProdutoById(updatedProduto.getId());
        BeanUtils.copyProperties(updatedProduto, produto);
        produtoRepository.save(produto);
    }

    public void deleteProduto(Long id) throws Exception {
        try {
            Produto produto = produtoRepository.findById(id).get();
            produtoRepository.delete(produto);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Produto (id = %d) não encontrado".formatted(id));
        }
    }

    public List<Produto> getAllProdutosByLojaId(Long lojaId) {
        return produtoRepository.findByLojaId(lojaId);
    }

    private Produto updateProperties(Produto produto, ProdutoDTO dto) {
        produto.setNome(dto.nome() != null ? dto.nome() : produto.getNome());
        produto.setDescricao(dto.descricao() != null ? dto.descricao() : produto.getDescricao());
        produto.setPreco(dto.preco() != null ? dto.preco() : produto.getPreco());
        produto.setSpecs(dto.specs() != null ? dto.specs() : produto.getSpecs());
        produto.setQtdeEstoque(dto.qtdeEstoque() != null ? dto.qtdeEstoque() : produto.getQtdeEstoque());
        return produto;
    }

}
