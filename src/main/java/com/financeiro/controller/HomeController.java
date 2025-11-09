package com.financeiro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para rota raiz da API.
 * Fornece informações sobre a API e links úteis.
 */
@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Controle Financeiro API");
        response.put("version", "1.0.0");
        response.put("description", "API RESTful para gerenciamento de finanças pessoais");
        response.put("docs", "/swagger-ui/index.html");
        response.put("apiDocs", "/api-docs");
        response.put("endpoints", Map.of(
            "transacoes", "/api/transacoes",
            "categorias", "/api/categorias"
        ));
        return ResponseEntity.ok(response);
    }
}

