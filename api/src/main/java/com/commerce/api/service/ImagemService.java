package com.commerce.api.service;

import com.commerce.api.controller.ImagemController;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Imagem;
import com.commerce.api.model.Loja;
import com.commerce.api.model.Produto;
import com.commerce.api.repository.ImagemRepository;
import com.commerce.api.repository.LojaRepository;
import com.commerce.api.repository.ProdutoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ImagemService {

    private final Path fileStorageLocation;
    private final ImagemRepository imagemRepository;
    private final ProdutoRepository produtoRepository;
    private final LojaRepository lojaRepository;
    private final AdminService adminService;

    public ImagemService(ImagemRepository imagemRepository, ProdutoRepository produtoRepository, LojaRepository lojaRepository, AdminService adminService) {
        this.imagemRepository = imagemRepository;
        this.produtoRepository = produtoRepository;
        this.lojaRepository = lojaRepository;
        this.adminService = adminService;
        this.fileStorageLocation = Paths.get("./storage/imagens/produtos/").toAbsolutePath().normalize();

    }

    public List<Imagem> adicionarImagens(String username, Long produtoId, MultipartFile[] imagemMultiPartArray) {
        List<Imagem> imagens = new ArrayList<>();
        Arrays.stream(imagemMultiPartArray).forEach(i -> {
            Imagem img = this.adicionarImagem(username, produtoId, i);
            if (img != null) imagens.add(img);
        });
        return imagens;
    }

    public Imagem adicionarImagem(String username, Long produtoId, MultipartFile imagemMultiPart) {
        if (!verificarPermissao(username, produtoId)) {
            System.out.println("adcionarImagem:deu ruim");
            return null;
        }
        System.out.println("ok" + imagemMultiPart.getName());

        Produto produto = this.produtoRepository.findById(produtoId).orElseThrow();
        Imagem imagem = multipartParaImagem(imagemMultiPart, produto);

        produto.adicionarImagem(imagem);
        produtoRepository.save(produto);
        Imagem saved = imagemRepository.save(imagem);
        saved.add(linkTo(methodOn(ImagemController.class).getImagemById(produtoId, saved.getId())).withSelfRel());
        return saved;
    }

    public List<Imagem> getImagensByProduto(Long produtoId, HttpServletRequest request) {
        List<Imagem> imagens = imagemRepository.findByProdutoId(produtoId);
        imagens.forEach(i -> {
            i.add(linkTo(methodOn(ImagemController.class).getImagemById(produtoId, i.getId())).withSelfRel());
            i.add(linkTo(methodOn(ImagemController.class).downloadImagem(produtoId, i.getId(), request)).withRel("download"));
        });
        return imagens;
    }

    public Imagem getImagemById(Long produtoId, Long imagemId) {
        Imagem img = this.imagemRepository.findById(imagemId).orElseThrow(() -> new ResourceNotFoundException("imagem não encontrada"));
        img.add(linkTo(methodOn(ImagemController.class).getImagemById(produtoId, img.getId())).withSelfRel());
        return img;
    }

    public Resource downloadImagem(Long produtoId, Long imagemId) {
        Imagem imagem = this.getImagemById(produtoId, imagemId);
        try {
            Path filePath = Paths.get(imagem.getPath());
            Resource resource = new UrlResource(filePath.toAbsolutePath().toUri());
            if (resource.exists()) return resource;
            else throw new FileNotFoundException("Imagem não encontrada");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void deleteImagem(String username, Long produtoId, Long imagemId) throws IOException {
        if (verificarPermissao(username, produtoId)) {
            Imagem imagem = this.getImagemById(produtoId, imagemId);
            try {
                if (imagem != null) Files.delete(Paths.get(imagem.getPath()));
            } catch (IOException ex) {
                throw new IOException("Imagem não está no servidor");
            }
            assert imagem != null;
            this.imagemRepository.delete(imagem);
        }
    }

    private Imagem multipartParaImagem(MultipartFile imagemMultiPart, Produto produto) {
        File imgFile = new File(this.fileStorageLocation + "/" + produto.getNome().replace(" ", "_"));
        if (!imgFile.exists()) {
            if (!imgFile.mkdir())
                throw new RuntimeException("Falha ao criar novo diretório em " + imgFile.getPath());
        }

        String path = imgFile.getPath() + "/" + imagemMultiPart.getOriginalFilename();
        Imagem imagem = new Imagem(imagemMultiPart.getOriginalFilename(), imagemMultiPart.getContentType(), path, produto);

        try {
            imagemMultiPart.transferTo(new File(path));
        } catch (IOException e) {
            throw new RuntimeException("Upload de imagem falhou " + e);
        }

        return imagem;
    }

    private boolean verificarPermissao(String username, Long produtoId) {
        if (adminService.isAdmin(username)) return true;
        Loja loja = lojaRepository.findByUsername(username);
        Produto produto = this.produtoRepository.findById(produtoId).orElseThrow();
        return produto.getLoja().equals(loja);
    }
}
