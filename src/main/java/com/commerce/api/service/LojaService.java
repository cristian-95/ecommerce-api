package com.commerce.api.service;

import com.commerce.api.controller.LojaController;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Loja;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.LojaDTO;
import com.commerce.api.model.dto.LojaUpdateDTO;
import com.commerce.api.repository.LojaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class LojaService {

    @Autowired
    private LojaRepository repository;
    @Autowired
    private ProdutoService produtoService;

    public List<Loja> getAllLojas() {
        List<Loja> lojas = repository.findAll();
        lojas.forEach(c -> c.add(linkTo(methodOn(LojaController.class).getById(c.getId())).withSelfRel()));
        return lojas;
    }

    public Loja getLojaById(Long id) throws ResourceNotFoundException {
        Loja loja = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada."));
        loja.add(linkTo(methodOn(LojaController.class).getAll()).withRel("Listagem"));
        return loja;
    }

    public Loja createLoja(LojaDTO dto) throws ResourceNotFoundException {
        Loja loja = new Loja(dto);
        String encriptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        loja.setPassword(encriptedPassword);
        Loja saved = repository.save(loja);
        saved.add(linkTo(methodOn(LojaController.class).getById(saved.getId())).withSelfRel());
        return saved;
    }


    public Loja updateLoja(LojaUpdateDTO dto) throws ResourceNotFoundException {
        Loja loja = getLojaById(dto.id());
        Loja updated = repository.save(updateProperties(loja, dto));
        updated.add(linkTo(methodOn(LojaController.class).getById(updated.getId())).withSelfRel());
        return updated;
    }

    public void deleteLoja(Long id) {
        try {
            Loja loja = repository.findById(id).get();
            repository.delete(loja);
        } catch (Exception e) {
            System.err.println("DELETE:Loja: %d não encontrado".formatted(id));
        }
    }

    public void adicionarProduto(Long lojaId, Long produtoId) throws ResourceNotFoundException {
        Loja loja = getLojaById(lojaId);
        Produto produto = produtoService.getProdutoById(produtoId);

        produto.setLoja(loja);
        loja.adicionarProduto(produto);

        produtoService.updateProduto(produto);
        repository.save(loja);

    }

    public void removerProduto(Long lojaId, Long produtoId) throws ResourceNotFoundException {
        Loja loja = getLojaById(lojaId);
        Produto produto = produtoService.getProdutoById(produtoId);

        produto.setLoja(null);
        loja.removerProduto(produto);

        produtoService.updateProduto(produto);
        repository.save(loja);

    }

    public List<Produto> getAllProdutos(Long lojaId) {
        return produtoService.getAllProdutosByLojaId(lojaId);
    }

    private Loja updateProperties(Loja loja, LojaUpdateDTO dto) {
        loja.setNome(dto.nome() != null ? dto.nome() : loja.getNome());
        loja.setCNPJ(dto.CNPJ() != null ? dto.CNPJ() : loja.getCNPJ());
        loja.setEmail(dto.email() != null ? dto.email() : loja.getEmail());
        loja.setTelefone(dto.telefone() != null ? dto.telefone() : loja.getTelefone());
        loja.setEndereco(dto.endereco() != null ? dto.endereco() : loja.getEndereco());
        return loja;
    }
}
