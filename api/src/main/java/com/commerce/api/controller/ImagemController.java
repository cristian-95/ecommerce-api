package com.commerce.api.controller;

import com.commerce.api.model.Imagem;
import com.commerce.api.security.TokenService;
import com.commerce.api.service.ImagemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/produtos/{produtoId}/imagens")
public class ImagemController {


    private final ImagemService imagemService;
    private final TokenService tokenService;

    public ImagemController(ImagemService imagemService, TokenService tokenService) {
        this.imagemService = imagemService;
        this.tokenService = tokenService;
    }

    @PostMapping
    @Operation(summary = "Adiciona uma imagem", tags = {"Produtos"})
    public ResponseEntity<Imagem> addImagem(@RequestHeader("Authorization") String token, @PathVariable Long produtoId, @RequestParam("file") MultipartFile file) {
        String username = tokenService.getUsernameFromToken(token);
        Imagem imagem = imagemService.adicionarImagem(username, produtoId, file);
        return new ResponseEntity<>(imagem, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Recupera imagens por produto", tags = {"Produtos"})
    public ResponseEntity<List<Imagem>> getImagensByProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(imagemService.getImagensByProduto(produtoId));
    }

    @GetMapping("/{imagemId}")
    @Operation(summary = "Recupera imagem por id", tags = {"Produtos"})
    public ResponseEntity<Imagem> getImagemById(@PathVariable Long produtoId, @PathVariable Long imagemId) {
        Imagem imagem = imagemService.getImagemById(produtoId, imagemId);
        return ResponseEntity.ok(imagem);
    }

    @GetMapping(value = "/download/{imagemId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    @Operation(summary = "Baixa imagem por id", tags = {"Produtos"})
    public ResponseEntity<Resource> downloadImagem(@PathVariable Long produtoId, @PathVariable Long imagemId, HttpServletRequest request) {
        Resource resource = imagemService.downloadImagem(produtoId, imagemId);

        String contentType = "";
        if (request != null) {
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (Exception e) {
                System.out.println("Não foi possivel determinar o tipo do arquivo");
            }
        }
        if (contentType.isBlank()) contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "correct-me" + "\"")
                .body(resource);
    }

    @DeleteMapping("/{imagemId}")
    @Operation(summary = "Remove imagem por id", tags = {"Produtos"})
    public ResponseEntity<Void> deleteImagem(@RequestHeader("Authorization") String token, @PathVariable Long produtoId, @PathVariable Long imagemId) {
        String username = tokenService.getUsernameFromToken(token);
        imagemService.deleteImagem(username, produtoId, imagemId);
        return ResponseEntity.noContent().build();
    }
}
