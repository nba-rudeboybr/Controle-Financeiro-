package com.financeiro;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot.
 * Microserviço de Controle Financeiro Pessoal.
 */
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Controle Financeiro API",
        version = "1.0.0",
        description = "API RESTful para gerenciamento de finanças pessoais",
        contact = @Contact(
            name = "Equipe Financeiro",
            email = "contato@financeiro.com"
        )
    )
)
public class ControleFinanceiroApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControleFinanceiroApiApplication.class, args);
    }
}

