package com.financeiro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Configuração do banco de dados para produção (Render).
 * Suporta DATABASE_URL no formato postgres:// ou JDBC direto.
 * Também suporta variáveis individuais DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD.
 */
@Configuration
@Profile("prod")
public class RenderDatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;
    
    @Value("${DATABASE_USERNAME:}")
    private String databaseUsername;
    
    @Value("${DATABASE_PASSWORD:}")
    private String databasePassword;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        // Se DATABASE_URL estiver no formato postgres:// ou postgresql://
        if (databaseUrl != null && !databaseUrl.isEmpty() && 
            (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://"))) {
            try {
                return createDataSourceFromPostgresUrl(databaseUrl);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Erro ao processar DATABASE_URL", e);
            }
        }
        
        // Se DATABASE_URL estiver no formato JDBC
        if (databaseUrl != null && !databaseUrl.isEmpty() && databaseUrl.startsWith("jdbc:postgresql://")) {
            return createDataSourceFromJdbcUrl(databaseUrl);
        }
        
        // Se tiver variáveis individuais
        if (databaseUsername != null && !databaseUsername.isEmpty() 
            && databasePassword != null && !databasePassword.isEmpty()
            && databaseUrl != null && !databaseUrl.isEmpty()) {
            return createDataSourceFromIndividualVars(databaseUrl, databaseUsername, databasePassword);
        }
        
        // Caso contrário, usar configuração padrão do Spring Boot
        return DataSourceBuilder.create().build();
    }

    private DataSource createDataSourceFromPostgresUrl(String databaseUrl) throws URISyntaxException {
        // Normalizar postgresql:// para postgres:// para parsing do URI
        String normalizedUrl = databaseUrl.replace("postgresql://", "postgres://");
        URI dbUri = new URI(normalizedUrl);
        
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost();
        
        // Adicionar porta se existir
        if (dbUri.getPort() != -1) {
            dbUrl += ":" + dbUri.getPort();
        }
        
        // Adicionar path (nome do banco)
        dbUrl += dbUri.getPath();
        
        // Para PostgreSQL com SSL (recomendado para produção)
        if (dbUrl.contains("?")) {
            dbUrl += "&sslmode=require";
        } else {
            dbUrl += "?sslmode=require";
        }
        
        return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url(dbUrl)
            .username(username)
            .password(password)
            .build();
    }
    
    private DataSource createDataSourceFromJdbcUrl(String jdbcUrl) {
        // Se já está em formato JDBC, usar diretamente
        String dbUrl = jdbcUrl;
        if (!dbUrl.contains("sslmode")) {
            if (dbUrl.contains("?")) {
                dbUrl += "&sslmode=require";
            } else {
                dbUrl += "?sslmode=require";
            }
        }
        
        DataSourceBuilder<?> builder = DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url(dbUrl);
            
        // Se tiver username e password separados, usar
        if (databaseUsername != null && !databaseUsername.isEmpty()) {
            builder.username(databaseUsername);
        }
        if (databasePassword != null && !databasePassword.isEmpty()) {
            builder.password(databasePassword);
        }
        
        return builder.build();
    }
    
    private DataSource createDataSourceFromIndividualVars(String url, String username, String password) {
        String dbUrl = url;
        // Se não for JDBC, assumir que precisa converter
        if (!dbUrl.startsWith("jdbc:")) {
            dbUrl = "jdbc:postgresql://" + dbUrl;
        }
        
        // Adicionar SSL se não tiver
        if (!dbUrl.contains("sslmode")) {
            if (dbUrl.contains("?")) {
                dbUrl += "&sslmode=require";
            } else {
                dbUrl += "?sslmode=require";
            }
        }
        
        return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url(dbUrl)
            .username(username)
            .password(password)
            .build();
    }
}

