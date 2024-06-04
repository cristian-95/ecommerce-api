package com.commerce.api.service;

import com.commerce.api.controller.LojaController;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Loja;
import com.commerce.api.model.Produto;
import com.commerce.api.model.Usuario;
import com.commerce.api.model.dto.LojaDTO;
import com.commerce.api.model.dto.LojaUpdateDTO;
import com.commerce.api.repository.LojaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class LojaService {

    @Autowired
    PagedResourcesAssembler<Loja> assembler;
    @Autowired
    private LojaRepository lojaRepository;
    @Autowired
    private ProdutoService produtoService;

    public Usuario createNewLojaAccount(Loja newLoja) {
        System.out.println("createNewLojaAccount");
        var saved = lojaRepository.save(newLoja);
        saved.add(linkTo(methodOn(LojaController.class).getById(saved.getId())).withSelfRel());
        return saved;
    }

    public Loja createLoja(LojaDTO dto) throws ResourceNotFoundException {
        Loja loja = new Loja(dto);
        String encriptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        loja.setPassword(encriptedPassword);
        Loja saved = lojaRepository.save(loja);
        saved.add(linkTo(methodOn(LojaController.class).getById(saved.getId())).withSelfRel());
        System.out.println("createLoja");
        return saved;
    }

    public Loja getProfile(String username) {
        return lojaRepository.findByUsername(username);
    }

    public PagedModel<EntityModel<Loja>> getAllLojas(Pageable pageable) {
        Page<Loja> lojas = lojaRepository.findAll(pageable);
        lojas.forEach(c -> c.add(linkTo(methodOn(LojaController.class).getById(c.getId())).withSelfRel()));
        System.out.println("getAllLojas");
        return assembler.toModel(lojas);

    }

    public Loja getLojaById(Long id) throws ResourceNotFoundException {
        Loja loja = lojaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada."));
        loja.add(linkTo(methodOn(LojaController.class).getAll(0, 0, "asc")).withRel("Listagem"));
        System.out.println("getLojaById");
        return loja;
    }


    public Loja updateLoja(String username, LojaUpdateDTO dto) throws ResourceNotFoundException {
        Loja loja = lojaRepository.findByUsername(username);
        Loja updated = lojaRepository.save(updateProperties(loja, dto));
        updated.add(linkTo(methodOn(LojaController.class).getById(updated.getId())).withSelfRel());
        System.out.println("updateLoja");
        return updated;
    }

    public void deleteLoja(Long id) {
        System.out.println("deleteLoja");
        try {
            Loja loja = lojaRepository.findById(id).get();
            lojaRepository.delete(loja);
        } catch (Exception e) {
            System.err.printf("DELETE:Loja: %d não encontrado%n", id);
        }
    }

    public PagedModel<EntityModel<Produto>> getAllProdutos(String username) {
        System.out.println("getAllProdutos");
        return produtoService.getAllProdutosByLoja(lojaRepository.findByUsername(username));
    }

    public void adicionarProduto(String username, Long produtoId) throws ResourceNotFoundException {
        System.out.println("adicionarProduto");
        Loja loja = lojaRepository.findByUsername(username);
        Produto produto = produtoService.getProdutoById(produtoId);

        produto.setLoja(loja);
        loja.adicionarProduto(produto);

        produtoService.save(produto);
        lojaRepository.save(loja);

    }

    public void removerProduto(String username, Long produtoId) throws ResourceNotFoundException {
        System.out.println("removerProduto");
        Loja loja = lojaRepository.findByUsername(username);
        Produto produto = produtoService.getProdutoById(produtoId);
        produto.setLoja(null);
        loja.removerProduto(produto);
        produtoService.deleteProduto(username, produtoId);
        lojaRepository.save(loja);
    }

    void save(Loja loja) {
        this.lojaRepository.save(loja);
    }

    private Loja updateProperties(Loja loja, LojaUpdateDTO dto) {
        System.out.println("updateProperties");
        loja.setNome(dto.nome() != null ? dto.nome() : loja.getNome());
        loja.setCNPJ(dto.CNPJ() != null ? dto.CNPJ() : loja.getCNPJ());
        loja.setEmail(dto.email() != null ? dto.email() : loja.getEmail());
        loja.setTelefone(dto.telefone() != null ? dto.telefone() : loja.getTelefone());
        loja.setEndereco(dto.endereco() != null ? dto.endereco() : loja.getEndereco());

        return loja;
    }

}
