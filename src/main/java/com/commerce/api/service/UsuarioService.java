package com.commerce.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Produto;
import com.commerce.api.model.Usuario;
import com.commerce.api.model.dto.UsuarioDTO;
import com.commerce.api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private ProdutoService produtoService;

    public List<Usuario> getAllUsuarios() {
        return repository.findAll();
    }

    public Usuario getUsuarioById(Long id) {
        try {
            Usuario usuario = repository.findById(id).get();
            return usuario;
        } catch (Exception e) {
            return null;
        }
    }

    public Usuario createUsuario(UsuarioDTO dto){
        Usuario usuario = new Usuario(dto);
        return repository.save(usuario);
    }

    public Usuario updateUsuario(UsuarioDTO dto){
        Usuario usuario = getUsuarioById(dto.id());
        usuario = update(usuario, dto);        
        return repository.save(usuario);
    }
    
    
    public void deleteUsuario(Long id) throws Exception {        
        try {
            Usuario usuario = repository.findById(id).get();
            repository.delete(usuario);
        } catch (Exception e) {
            System.err.println("DELETE: Usuario: %d não encontrado".formatted(id));
        }
    }

    public Produto addFavorito(Long userId, Long produtoId) throws ResourceNotFoundException {
        Usuario usuario = getUsuarioById(userId);
        Produto produto = produtoService.getProdutoById(produtoId);
        usuario.addFavorito(produto);
        repository.save(usuario);
        return produto;
    }

    public Produto removeFavorito(Long userId, Long produtoId) throws ResourceNotFoundException {
        Usuario usuario = getUsuarioById(userId);
        Produto produto = produtoService.getProdutoById(produtoId);
        usuario.removeFavorito(produto);
        repository.save(usuario);
        return produto;
    }
    
    public List<Produto> getAllFavoritos(Long userId) {
        Usuario usuario = getUsuarioById(userId);               
        return usuario.getFavoritos();
    }
    
    private Usuario update(Usuario usuario, UsuarioDTO dto) {
        usuario.setNome(dto.nome()!= null ? dto.nome() : usuario.getNome());
        usuario.setSobrenome(dto.sobrenome()!= null ? dto.sobrenome() : usuario.getSobrenome());
        usuario.setDocumento(dto.documento()!= null ? dto.documento() : usuario.getDocumento());
        usuario.setEmail(dto.email()!= null ? dto.email() : usuario.getEmail());
        usuario.setEndereco(dto.endereco()!= null ? dto.endereco() : usuario.getEndereco());
        usuario.setDataNasc(dto.dataNasc()!= null ? dto.dataNasc() : usuario.getDataNasc());
        usuario.setGenero(dto.genero()!= null ? dto.genero() : usuario.getGenero());
        return usuario;
    }
}
