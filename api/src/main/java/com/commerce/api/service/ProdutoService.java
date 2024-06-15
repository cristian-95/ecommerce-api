package com.commerce.api.service;

import com.commerce.api.controller.ImagemController;
import com.commerce.api.controller.ProdutoController;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Admin;
import com.commerce.api.model.Imagem;
import com.commerce.api.model.Loja;
import com.commerce.api.model.Produto;
import com.commerce.api.model.dto.ProdutoDTO;
import com.commerce.api.model.dto.RequestDTO;
import com.commerce.api.repository.LojaRepository;
import com.commerce.api.repository.ProdutoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProdutoService {

    private final PagedResourcesAssembler<Produto> assembler;
    private final ProdutoRepository produtoRepository;
    private final LojaRepository lojaRepository;
    private final AdminService adminService;
    private final ImagemService imagemService;

    public ProdutoService(PagedResourcesAssembler<Produto> assembler, ProdutoRepository produtoRepository, LojaRepository lojaRepository, AdminService adminService, ImagemService imagemService) {
        this.assembler = assembler;
        this.produtoRepository = produtoRepository;
        this.lojaRepository = lojaRepository;
        this.adminService = adminService;
        this.imagemService = imagemService;
    }

    public PagedModel<EntityModel<Produto>> getAllProdutos(Pageable pageable, String searchKey, HttpServletRequest request) {
        Page<Produto> produtos;
        if (searchKey.isBlank()) {
            produtos = produtoRepository.findAll(pageable);
        } else {
            produtos = produtoRepository
                    .findByNomeOrDescricao(searchKey, searchKey, pageable);
        }
        produtos.forEach(p -> {
            p.add(linkTo(methodOn(ProdutoController.class).getById(p.getId())).withSelfRel());
            p.getImagens().forEach( i-> i.add(linkTo(methodOn(ImagemController.class).downloadImagem(p.getId(), i.getId(),request)).withRel("download")));

        });
        return assembler.toModel(produtos);
    }

    public Produto getProdutoById(Long id) throws ResourceNotFoundException {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        produto.add(linkTo(methodOn(ProdutoController.class).getAll(0, 0, "", "", null)).withRel("Listagem"));

        return produto;
    }

    public Produto createProduto(String username, ProdutoDTO dto, MultipartFile[] imagens) {
        Loja loja;
        if (adminService.isAdmin(username) && dto.lojaId() != null) {
            Optional<Loja> l = lojaRepository.findById(dto.lojaId());
            loja = l.orElse(null);
        } else {
            loja = lojaRepository.findByUsername(username);
        }
        Produto produto = new Produto(dto);
        produto.setLoja(loja);
        Produto saved = produtoRepository.save(produto);

        if (imagens.length > 0) {
            List<Imagem> imagemList = new ArrayList<>();
            System.out.println("tem imagem");
            System.out.println(imagens.length);
            for (MultipartFile img : imagens) {
                Imagem imagem = imagemService.adicionarImagem(username, saved.getId(), img);
                System.out.println("imagemService: " + imagem);
                imagemList.add(imagem);

            }
            imagemList.forEach(System.out::println);
        }
        saved = produtoRepository.save(produto);
        saved.add(linkTo(methodOn(ProdutoController.class).getById(saved.getId())).withSelfRel());
        return saved;
    }

    public Produto updateProduto(String username, ProdutoDTO dto) throws ResourceNotFoundException {
        if (!verificarPermissao(username, dto.id())) {
            return null;
        }
        Loja loja = lojaRepository.findByUsername(username);
        Admin admin = adminService.getProfile(username);
        Produto produto = getProdutoById(dto.id());
        Produto updated = produtoRepository.save(updateProperties(produto, dto));
        updated.add(linkTo(methodOn(ProdutoController.class).getById(updated.getId())).withSelfRel());
        return updated;

    }

    public void deleteProduto(String username, RequestDTO requestDTO) {
        if (verificarPermissao(username, requestDTO.id())) {
            Produto produto = produtoRepository.findById(requestDTO.id()).get();
            produtoRepository.delete(produto);
        }
    }

    public PagedModel<EntityModel<Produto>> getAllProdutosByLoja(Loja loja) {
        List<Produto> produtos = produtoRepository.findByLojaId(loja.getId());
        produtos.forEach(p -> p.add(linkTo(methodOn(ProdutoController.class).getById(p.getId())).withSelfRel()));
        Page<Produto> page = new PageImpl<>(produtos);
        return assembler.toModel(page);
    }

    public void save(Produto produto) {
        this.produtoRepository.save(produto);
    }

    private Produto updateProperties(Produto produto, ProdutoDTO dto) {
        produto.setNome(dto.nome() != null ? dto.nome() : produto.getNome());
        produto.setDescricao(dto.descricao() != null ? dto.descricao() : produto.getDescricao());
        produto.setPreco(dto.preco() != null ? dto.preco() : produto.getPreco());
        produto.setSpecs(dto.specs() != null ? dto.specs() : produto.getSpecs());
        produto.setQtdeEstoque(dto.qtdeEstoque() != null ? dto.qtdeEstoque() : produto.getQtdeEstoque());
        return produto;
    }

    private boolean verificarPermissao(String username, Long produtoId) {
        if (!adminService.isAdmin(username) || !lojaRepository.existsByUsername(username)) return false;
        Loja loja = lojaRepository.findByUsername(username);
        Produto produto = this.produtoRepository.findById(produtoId).orElseThrow();
        return produto.getLoja().equals(loja);
    }
}
