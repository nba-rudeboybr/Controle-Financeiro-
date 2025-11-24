package com.financeiro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Teste de contexto da aplicação.
 */
@SpringBootTest
@ActiveProfiles("dev")
class ControleFinanceiroApiApplicationTests {

    @Test
    void contextLoads() {
        // Verifica se o contexto da aplicação carrega corretamente
    }
}

