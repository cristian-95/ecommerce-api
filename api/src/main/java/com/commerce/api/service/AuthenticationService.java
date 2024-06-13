package com.commerce.api.service;

import com.commerce.api.repository.AdminRepository;
import com.commerce.api.repository.ClienteRepository;
import com.commerce.api.repository.LojaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final ClienteRepository clienteRepository;
    private final LojaRepository lojaRepository;
    private final AdminRepository adminRepository;

    public AuthenticationService(ClienteRepository clienteRepository, LojaRepository lojaRepository, AdminRepository adminRepository) {
        this.clienteRepository = clienteRepository;
        this.lojaRepository = lojaRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user;
        if (adminRepository.existsByUsername(username)) {
            user = adminRepository.findByUsername(username);
        } else if (lojaRepository.existsByUsername(username)) {
            user = lojaRepository.findByUsername(username);
        } else {
            user = clienteRepository.findByUsername(username);
        }
        return user;
    }
}
