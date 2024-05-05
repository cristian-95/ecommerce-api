package com.commerce.api.service;

import com.commerce.api.repository.ClienteRepository;
import com.commerce.api.repository.LojaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private LojaRepository lojaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user;
        if (clienteRepository.existsByUsername(username)) {
            user = clienteRepository.findByUsername(username);
        } else {
            user = lojaRepository.findByUsername(username);
        }
        return user;
    }
}
