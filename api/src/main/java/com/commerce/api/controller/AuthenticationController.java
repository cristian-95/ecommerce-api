package com.commerce.api.controller;

import com.commerce.api.model.*;
import com.commerce.api.model.dto.AuthenticationDTO;
import com.commerce.api.model.dto.LoginResponseDTO;
import com.commerce.api.model.dto.RegisterDTO;
import com.commerce.api.security.TokenService;
import com.commerce.api.service.AdminService;
import com.commerce.api.service.ClienteService;
import com.commerce.api.service.LojaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Endpoint de autenticação para login e registro de novos usuários.")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final ClienteService clienteService;
    private final LojaService lojaService;
    private final AdminService adminService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService, ClienteService clienteService, LojaService lojaService, AdminService adminService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.clienteService = clienteService;
        this.lojaService = lojaService;
        this.adminService = adminService;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Entrar na conta", tags = {"Autenticação"})
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        try {
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((Usuario) auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDTO(auth.getAuthorities(), token));
        } catch (Exception e) {
            System.err.println("Erro: " + e + "\n " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Criar conta", tags = {"Autenticação"})
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        Usuario user;
        String encryptedPassword;

        switch (UserRole.valueOf(data.role().toUpperCase())) {
            case USER -> {
                if (this.clienteService.getProfile(data.username()) != null)
                    return ResponseEntity.badRequest().build();
                encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
                Cliente newCliente = new Cliente(data.username(), encryptedPassword, data.role().toUpperCase());
                user = clienteService.createNewClienteAccount(newCliente);
            }
            case MANAGER -> {

                if (this.lojaService.getProfile(data.username()) != null)
                    return ResponseEntity.badRequest().build();

                encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
                Loja newLoja = new Loja(data.username(), encryptedPassword, data.role().toUpperCase());
                user = lojaService.createNewLojaAccount(newLoja);
            }
            case ADMIN -> {
                if (this.adminService.getProfile(data.username()) != null)
                    return ResponseEntity.badRequest().build();

                encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
                Admin newAdmin = new Admin(data.username(), encryptedPassword, data.role().toUpperCase());
                user = adminService.createNewAdminAccount(newAdmin);
            }
            default -> user = null;
        }

        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }
}