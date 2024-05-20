package com.commerce.api.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce API")
                        .version("1.0.0")
                        .description("""
                                    API RESTful para o gerenciamento de uma plataforma de e-commerce.
                                    
                                    Curso: Bacharelado em Ciência da Computação
                                    Disciplina: Desenvolvimento para Web
                                    Professor: Davi Duarte de Paula
                                    
                                    Grupo:
                                    Cristian Santos de Castro
                                    Edu Teodoro Batista
                                    José Zumbini Neto
                                    Sergio Paulo Guerrato Alves
                                     
                                """)
                        .license(
                                new License()
                                        .name("Apache 2.0")
                                        .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                );
    }

}
