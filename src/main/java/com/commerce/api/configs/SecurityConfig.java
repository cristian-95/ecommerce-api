package com.commerce.api.configs;

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
                        .requestMatchers(HttpMethod.GET, "/produtos*").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/produtos").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/produtos").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/produtos*").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/clientes*").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/clientes").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/clientes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/clientes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/clientes*").hasRole("ADMIN")
                        .requestMatchers("/clientes/*").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/lojas*").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/lojas").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/lojas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/lojas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/lojas*").hasRole("ADMIN")
                        .requestMatchers("/lojas/*").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers( "/").permitAll()                        
                        .requestMatchers( "/swagger-ui/**").permitAll()                        
                        .requestMatchers( "/v3/api-docs/**").permitAll()                        
                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /***************** Endpoints: *******************
     * /
     * /clientes,
     * /clientes/{id},
     * /clientes/{id}/favoritos,
     * /clientes/{id}/pedidos,
     * /clientes/{id},
     * /lojas,
     * /lojas/{id},
     * /lojas/{id}/produtos,
     * /produtos,
     * /produtos/{id},
     * /carrinho,
     * /carrinho/{id},
     * /carrinho/{id}/{operacao}/{produtoId},
     * /pedidos,
     * /pedidos/{id}
     * /auth/login
     * /auth/register
     ***********************************************/

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
