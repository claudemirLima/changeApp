package com.exchange.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Exchange API - Reino SRM")
                .description("API de conversão de moedas para o reino SRM. " +
                           "Gerencia taxas de câmbio entre Ouro Real e Tibar, " +
                           "com suporte a conversões específicas por produto.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Desenvolvedores SRM")
                    .email("dev@srm-reino.com")
                    .url("https://srm-reino.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8081")
                    .description("Servidor de Desenvolvimento"),
                new Server()
                    .url("https://api.srm-reino.com")
                    .description("Servidor de Produção")
            ));
    }
} 