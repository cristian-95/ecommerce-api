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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private LojaService lojaService;
    @Autowired
    private AdminService adminService;

    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Entrar na conta", description = "Autenticação de usuários.", tags = {
            "Autenticação"}, responses = {
            @ApiResponse(description = "Sucess", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LoginResponseDTO.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        try {
            System.out.println("68");
            var auth = this.authenticationManager.authenticate(usernamePassword);
            System.out.println("70");
            var token = tokenService.generateToken((Usuario) auth.getPrincipal());
            System.out.println("72");
            return ResponseEntity.ok(new LoginResponseDTO(auth.getAuthorities(), token));
        } catch (Exception e) {
            System.err.println("Erro: " + e + "\n " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Criar conta", description = "Cria uma conta.", tags = {
            "Autenticação"}, responses = {
            @ApiResponse(description = "Sucess", responseCode = "200", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
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