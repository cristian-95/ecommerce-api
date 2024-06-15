package com.commerce.api.service;

import com.commerce.api.exception.ResourceNotFoundException;
import com.commerce.api.model.Admin;
import com.commerce.api.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin createNewAdminAccount(Admin newAdmin) {
        return this.adminRepository.save(newAdmin);
    }

    public Admin getAdminById(Long id) {
        return this.adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin não encontrado"));
    }

    public Admin getProfile(String username) {
        return this.adminRepository.findByUsername(username);
    }

    public Boolean isAdmin(String username) {
        return this.adminRepository.existsByUsername(username);
    }
}
