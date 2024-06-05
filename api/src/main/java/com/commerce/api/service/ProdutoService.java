package com.commerce.api.service;

import com.commerce.api.controller.ProdutoController;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Loja;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.ProdutoDTO;
import com.commerce.api.model.dto.RequestDTO;
import com.commerce.api.repository.LojaRepository;
import com.commerce.api.repository.ProdutoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProdutoService {

    @Autowired
    PagedResourcesAssembler<Produto> assembler;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private LojaRepository lojaRepository;

    public PagedModel<EntityModel<Produto>> getAllProdutos(Pageable pageable) {
        Page<Produto> produtos = produtoRepository.findAll(pageable);
        produtos.forEach(p -> p.add(linkTo(methodOn(ProdutoController.class).getById(p.getId())).withSelfRel()));
        return assembler.toModel(produtos);
    }

    public Produto getProdutoById(Long id) throws ResourceNotFoundException {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        produto.add(linkTo(methodOn(ProdutoController.class).getAll(0, 0, "")).withRel("Listagem"));
        return produto;
    }

    public Produto createProduto(String username, ProdutoDTO dto) {
        Loja loja = lojaRepository.findByUsername(username);
        Produto produto = new Produto(dto);
        produto.setLoja(loja);
        Produto saved = produtoRepository.save(produto);
        saved.add(linkTo(methodOn(ProdutoController.class).getById(saved.getId())).withSelfRel());
        return saved;
    }

    public Produto updateProduto(String username, ProdutoDTO dto) throws ResourceNotFoundException {
        Loja loja = lojaRepository.findByUsername(username);
        Produto produto = getProdutoById(dto.id());
        if (loja.equals(produto.getLoja())) {
            Produto updated = produtoRepository.save(updateProperties(produto, dto));
            updated.add(linkTo(methodOn(ProdutoController.class).getById(updated.getId())).withSelfRel());
            return updated;
        } else {
            return null;
        }
    }

    public void updateProduto(String username, Produto updatedProduto) throws ResourceNotFoundException {
        Loja loja = lojaRepository.findByUsername(username);
        Produto produto = getProdutoById(updatedProduto.getId());
        if (loja.equals(updatedProduto.getLoja())) {
            BeanUtils.copyProperties(updatedProduto, produto);
            produtoRepository.save(produto);
        }
    }

    public void deleteProduto(String username, RequestDTO requestDTO) {
        Loja loja = lojaRepository.findByUsername(username);
        try {
            Produto produto = produtoRepository.findById(requestDTO.id()).get();
            if (loja.equals(produto.getLoja())) {
                produtoRepository.delete(produto);
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("Produto (id = %d) não encontrado".formatted(requestDTO.id()));
        }
    }

    public PagedModel<EntityModel<Produto>> getAllProdutosByLoja(Loja loja) {
        List<Produto> produtos = produtoRepository.findByLojaId(loja.getId());
        produtos.forEach(p -> p.add(linkTo(methodOn(ProdutoController.class).getById(p.getId())).withSelfRel()));
        Page<Produto> page = new PageImpl<>(produtos);
        return assembler.toModel(page);
    }

    private Produto updateProperties(Produto produto, ProdutoDTO dto) {
        produto.setNome(dto.nome() != null ? dto.nome() : produto.getNome());
        produto.setDescricao(dto.descricao() != null ? dto.descricao() : produto.getDescricao());
        produto.setPreco(dto.preco() != null ? dto.preco() : produto.getPreco());
        produto.setSpecs(dto.specs() != null ? dto.specs() : produto.getSpecs());
        produto.setQtdeEstoque(dto.qtdeEstoque() != null ? dto.qtdeEstoque() : produto.getQtdeEstoque());
        return produto;
    }

    public void save(Produto produto) {
        this.produtoRepository.save(produto);
    }
}
