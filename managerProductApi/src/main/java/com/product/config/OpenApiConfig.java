package com.product.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do OpenAPI/Swagger para o ManagerProductApi
 */
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Manager Product API")
                .description("API para gerenciamento de produtos e reinos do sistema ChangeApp")
                .version("1.0.0")
                .contact(new Contact()
                    .name("ChangeApp Team")
                    .email("team@changeapp.com")
                    .url("https://changeapp.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8083")
                    .description("Servidor de Desenvolvimento"),
                new Server()
                    .url("https://api.changeapp.com")
                    .description("Servidor de Produção")
            ));
    }
} 