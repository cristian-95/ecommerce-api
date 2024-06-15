package com.commerce.api.controller;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Admin;
import com.commerce.api.security.TokenService;
import com.commerce.api.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService, TokenService tokenService) {
        this.adminService = adminService;
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


}
