package com.commerce.api.service;

import com.commerce.api.controller.AdminController;
import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Admin;
import com.commerce.api.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

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
}
