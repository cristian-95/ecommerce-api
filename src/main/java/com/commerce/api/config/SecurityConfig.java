package com.commerce.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.commerce.api.security.SecurityFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/produtos").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/produtos").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PUT,"/produtos").hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE,"/produtos*").hasRole("MANAGER")
                .requestMatchers(HttpMethod.GET,"/produtos*").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/usuarios").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/usuarios").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/usuarios*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/usuarios*").hasRole("USER")
                .requestMatchers("/usuarios/*").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
