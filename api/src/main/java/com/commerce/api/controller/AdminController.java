package com.commerce.api.controller;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Admin;
import com.commerce.api.model.Cliente;
import com.commerce.api.model.dto.AdminUpdateDTO;
import com.commerce.api.security.TokenService;
import com.commerce.api.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private  final TokenService tokenService;

    public AdminController(AdminService adminService, TokenService tokenService) {
        this.adminService = adminService;
        this.tokenService = tokenService;
    }

    public ResponseEntity<?> getById(Long id) {
        Admin admin;
        try {
            admin = adminService.getAdminById(id);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(admin);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Atualiza um perfil de Administrador", tags = {"Admin"}, responses = {
            @ApiResponse(description = "Sucess", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)})
    public ResponseEntity<Admin> update(@RequestHeader(name = "Authorization") String token, @RequestBody @Valid AdminUpdateDTO dto) throws ResourceNotFoundException {
        String username = tokenService.getUsernameFromToken(token);
        return ResponseEntity.ok(adminService.updateProfile(username, dto));
    }
}
