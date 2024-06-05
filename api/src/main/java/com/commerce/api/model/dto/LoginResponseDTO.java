package com.commerce.api.model.dto;

public record LoginResponseDTO(
        java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> role,
        String token) {
}
