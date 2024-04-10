package com.commerce.api.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SpringDocConfig {

    @Bean    
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce API")
                        .version("1.0.0")
                        .description("""
                            Api Rest para o gerenciamento de uma plataforma de ecommerce.
                            Grupo: Cristian, Edu, José e Sergio
                        """)
                        .license(
                                new License()
                                        .name("Apache 2.0")
                                        .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                );
    }

}
