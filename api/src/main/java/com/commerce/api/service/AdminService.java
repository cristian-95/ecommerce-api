package com.commerce.api.service;

import com.commerce.api.controller.AdminController;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Admin;
import com.commerce.api.model.dto.AdminUpdateDTO;
import com.commerce.api.repository.AdminRepository;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin createNewAdminAccount(Admin newAdmin) {
        var saved = this.adminRepository.save(newAdmin);
        saved.add(linkTo(methodOn(AdminController.class).getById(saved.getId())).withSelfRel());
        return saved;
    }

    public Admin getAdminById(Long id) {
        return this.adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin não encontrado"));
    }

    public Admin getProfile(String username) {
        return this.adminRepository.findByUsername(username);
    }

    public Admin updateProfile(String username, AdminUpdateDTO dto) {
        return null;
    }

    public Boolean isAdmin(String username) {
        return this.adminRepository.existsByUsername(username);
    }
}
