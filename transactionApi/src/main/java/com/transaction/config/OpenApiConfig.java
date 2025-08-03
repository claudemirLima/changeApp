package com.transaction.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do OpenAPI/Swagger para TransactionApi
 */
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TransactionApi - Sistema de Transações")
                        .description("API para gerenciamento de transações comerciais do reino SRM")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe SRM")
                                .email("srm@kingdom.com")
                                .url("https://srm-kingdom.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8082")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.srm-kingdom.com")
                                .description("Servidor de Produção")
                ));
    }
} 