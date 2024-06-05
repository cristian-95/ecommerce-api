package com.commerce.api.security;

import com.commerce.api.repository.AdminRepository;
import com.commerce.api.repository.ClienteRepository;
import com.commerce.api.repository.LojaRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private LojaRepository lojaRepository;
    @Autowired
    private AdminRepository adminRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String token = this.recoverToken(request);
        if (token != null) {
            var username = tokenService.validateToken(token);
            UserDetails user;

            if (clienteRepository.existsByUsername(username)) {
                user = clienteRepository.findByUsername(username);
            } else if (lojaRepository.existsByUsername(username)) {
                user = lojaRepository.findByUsername(username);
            } else {
                user = adminRepository.findByUsername(username);
            }
            if (user != null) {
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        return authHeader != null ? authHeader.replace("Bearer ", "") : authHeader;
    }

}
