package com.commerce.api.configs;

import com.commerce.api.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/produtos**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/produtos**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/produtos**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/produtos**").hasRole("MANAGER")

                        .requestMatchers(HttpMethod.GET, "/clientes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/clientes**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/clientes**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/clientes/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/clientes/{id}").hasRole("USER")
                        .requestMatchers("/clientes/{id}/carrinho").hasRole("USER")
                        .requestMatchers("/clientes/{id}/favoritos").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/clientes/{id}/pedidos").hasRole("USER")

                        .requestMatchers(HttpMethod.DELETE, "/lojas*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/lojas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/lojas**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/lojas**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/lojas/{id}/produtos").hasAnyRole(new String[]{"USER", "MANAGER"})
                        .requestMatchers(HttpMethod.POST, "/lojas/{id}/produtos").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/lojas/{id}/produtos").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/lojas/{id}/pedidos").hasRole("MANAGER")


                        .requestMatchers(HttpMethod.POST, "/pedidos").hasAnyRole(new String[]{"USER", "MANAGER"})
                        .requestMatchers("/pedidos/{id}").hasRole("ADMIN")
                        .requestMatchers("/pedidos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/pedidos/{id}").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/pedidos/{id}").hasRole("MANAGER")

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
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
