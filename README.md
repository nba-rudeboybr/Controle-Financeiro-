# 💰 Controle Financeiro API - Microserviço Spring Boot

## 📚 Descrição

Microserviço RESTful desenvolvido com Spring Boot para **gerenciamento de finanças pessoais**. A aplicação permite o controle completo de receitas e despesas, categorização de transações, geração de relatórios financeiros e análise de saldo, aplicando conceitos avançados de Programação Orientada a Objetos (POO) e boas práticas de desenvolvimento.

Este projeto foi desenvolvido como trabalho acadêmico de Spring Boot, integrando o **frontend existente** (HTML/CSS/JavaScript) com um **backend robusto** em Java.

## 🎯 Funcionalidades

### Gestão de Transações
- ✅ **CRUD Completo** de transações (receitas e despesas)
- ✅ **Categorização** de transações por tipo
- ✅ **Filtros avançados** por período, tipo, categoria e descrição
- ✅ **Validação robusta** de dados de entrada

### Gestão de Categorias
- ✅ **CRUD Completo** de categorias
- ✅ **Categorias por tipo** (receita ou despesa)
- ✅ **Cores personalizadas** para visualização

### Relatórios Financeiros
- ✅ **Resumo financeiro** com cálculo automático de:
  - Total de receitas
  - Total de despesas  
  - Saldo (receitas - despesas)
  - Quantidade de transações
- ✅ **Análise por período** customizado
- ✅ **Estatísticas detalhadas**

### Recursos Técnicos
- ✅ **Documentação interativa** com Swagger/OpenAPI
- ✅ **Testes automatizados** com cobertura de 90%+
- ✅ **Tratamento de erros** padronizado
- ✅ **Deploy pronto** para produção

## 🏗️ Arquitetura

O projeto segue o padrão arquitetural em camadas do Spring Boot:

```
com.financeiro
├── controller/          # Camada de apresentação (REST Controllers)
│   ├── TransacaoController.java      # 10 endpoints de transações
│   └── CategoriaController.java      # 5 endpoints de categorias
│
├── service/            # Camada de negócio (lógica financeira)
│   ├── TransacaoService.java        # Lógica de transações e cálculos
│   └── CategoriaService.java        # Lógica de categorias
│
├── repository/         # Camada de acesso a dados (JPA Repositories)
│   ├── TransacaoRepository.java     # Queries customizadas
│   └── CategoriaRepository.java     # Persistência de categorias
│
├── model/             # Entidades JPA
│   ├── Transacao.java              # Entidade de transação
│   ├── Categoria.java              # Entidade de categoria
│   └── TipoTransacao.java          # Enum (RECEITA/DESPESA)
│
├── dto/               # Data Transfer Objects
│   ├── TransacaoRequestDTO.java    # DTO de entrada
│   ├── TransacaoResponseDTO.java   # DTO de saída
│   ├── CategoriaDTO.java           # DTO de categoria
│   └── ResumoFinanceiroDTO.java    # DTO de resumo
│
├── exception/         # Exceções e tratamento global
│   ├── ResourceNotFoundException.java
│   ├── BusinessException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
│
└── ControleFinanceiroApiApplication.java  # Classe principal
```

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
  - Spring Web (REST API)
  - Spring Data JPA (Persistência)
  - Spring Validation (Validações)
- **Banco de Dados**
  - H2 (desenvolvimento/testes)
  - PostgreSQL (produção)

### Documentação e Testes
- **Springdoc OpenAPI 2.3.0** (Swagger)
- **JUnit 5** (testes unitários)
- **Mockito** (mock de dependências)
- **JaCoCo** (cobertura de código 90%+)

### Ferramentas
- **Maven** (gerenciamento de dependências)
- **Docker** (containerização)
- **Git** (controle de versão)

### Deploy
- **Render** (plataforma de nuvem - plano free disponível)
- **PostgreSQL** (banco de produção)

## 📋 Pré-requisitos

Para executar o projeto localmente:

- Java JDK 17 ou superior
- Maven 3.6+
- Git

## 🔧 Instalação e Execução Local

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/controle-financeiro-api.git
cd controle-financeiro-api
```

### 2. Compile o projeto

```bash
mvn clean install
```

### 3. Execute a aplicação (ambiente de desenvolvimento)

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### 4. Acesse a documentação da API

Após iniciar a aplicação:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/api-docs
- **Frontend**: Abra o `index.html` no navegador

### 5. Console H2 (apenas em desenvolvimento)

Para acessar o banco de dados H2 em memória:
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:financeiro`
- **Username**: `sa`
- **Password**: (deixe em branco)

## 📡 Endpoints da API

### 🏦 Transações Financeiras

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/transacoes` | Criar nova transação |
| GET | `/api/transacoes` | Listar todas as transações |
| GET | `/api/transacoes/{id}` | Buscar transação por ID |
| PUT | `/api/transacoes/{id}` | Atualizar transação |
| DELETE | `/api/transacoes/{id}` | Deletar transação |
| GET | `/api/transacoes/tipo/{tipo}` | Buscar por tipo (RECEITA/DESPESA) |
| GET | `/api/transacoes/periodo` | Buscar por período |
| GET | `/api/transacoes/categoria/{id}` | Buscar por categoria |
| GET | `/api/transacoes/resumo` | Obter resumo financeiro |
| GET | `/api/transacoes/buscar` | Buscar por descrição |

### 📊 Categorias

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/categorias` | Criar nova categoria |
| GET | `/api/categorias` | Listar todas as categorias |
| GET | `/api/categorias/{id}` | Buscar categoria por ID |
| GET | `/api/categorias/tipo/{tipo}` | Buscar categorias por tipo |
| PUT | `/api/categorias/{id}` | Atualizar categoria |
| DELETE | `/api/categorias/{id}` | Deletar categoria |

**Total: 16 rotas** (requisito: mínimo 6) ✅

## 💡 Exemplos de Uso

### Criar uma categoria de DESPESA

```bash
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Alimentação",
    "descricao": "Gastos com alimentação e restaurantes",
    "tipo": "DESPESA",
    "cor": "#FF5733"
  }'
```

### Criar uma transação (despesa)

```bash
curl -X POST http://localhost:8080/api/transacoes \
  -H "Content-Type: application/json" \
  -d '{
    "descricao": "Almoço no restaurante",
    "valor": 150.50,
    "tipo": "DESPESA",
    "data": "2025-10-28",
    "categoriaId": 1,
    "observacoes": "Pagamento via cartão"
  }'
```

### Criar uma transação (receita)

```bash
curl -X POST http://localhost:8080/api/transacoes \
  -H "Content-Type: application/json" \
  -d '{
    "descricao": "Salário Outubro",
    "valor": 5000.00,
    "tipo": "RECEITA",
    "data": "2025-10-05",
    "categoriaId": 1,
    "observacoes": "Salário mensal"
  }'
```

### Listar todas as transações

```bash
curl http://localhost:8080/api/transacoes
```

### Buscar transações por período

```bash
curl "http://localhost:8080/api/transacoes/periodo?dataInicio=2025-10-01&dataFim=2025-10-31"
```

### Obter resumo financeiro

```bash
curl "http://localhost:8080/api/transacoes/resumo?dataInicio=2025-10-01&dataFim=2025-10-31"
```

**Resposta:**
```json
{
  "totalReceitas": 5000.00,
  "totalDespesas": 3200.50,
  "saldo": 1799.50,
  "quantidadeTransacoes": 25,
  "dataInicio": "2025-10-01",
  "dataFim": "2025-10-31"
}
```

### Buscar transações por tipo

```bash
curl http://localhost:8080/api/transacoes/tipo/DESPESA
```

### Deletar uma transação

```bash
curl -X DELETE http://localhost:8080/api/transacoes/1
```

## 🧪 Testes

### Executar todos os testes

```bash
mvn test
```

### Gerar relatório de cobertura (JaCoCo)

```bash
mvn clean test jacoco:report
```

O relatório será gerado em: `target/site/jacoco/index.html`

### Verificar cobertura mínima (90%)

```bash
mvn clean verify
```

Se a cobertura estiver abaixo de 90%, o build falhará.

### Estatísticas de Testes

- **14 testes** para TransacaoService
- **8 testes** para TransacaoController
- **1 teste** de contexto da aplicação
- **Total: 23+ testes** com cobertura de 90%+

## 🐳 Docker

### Construir a imagem Docker

```bash
docker build -t controle-financeiro-api .
```

### Executar com Docker

```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILE=prod \
  -e DATABASE_URL=jdbc:postgresql://seu-host:5432/financeiro \
  -e DATABASE_USERNAME=seu-usuario \
  -e DATABASE_PASSWORD=sua-senha \
  controle-financeiro-api
```

## ☁️ Deploy em Produção

### 🚀 Deploy no Render

O Render oferece plano free com PostgreSQL incluído, ideal para este projeto.

#### Método Rápido (usando render.yaml):

1. **Criar conta no Render**: https://render.com
2. **Conectar repositório GitHub/GitLab** no dashboard
3. **Selecionar "Blueprint"** e conectar o repositório
4. **Render detectará automaticamente** o arquivo `render.yaml`
5. **Criar serviços** (Web Service + PostgreSQL)
6. **Deploy automático!**

#### Método Manual:

1. **Criar PostgreSQL Database**:
   - No dashboard Render, clique em "New +" > "PostgreSQL"
   - Configure: Name, Database, User
   - Plan: Free
   - Clique em "Create Database"

2. **Criar Web Service**:
   - No dashboard Render, clique em "New +" > "Web Service"
   - Conecte seu repositório GitHub/GitLab
   - Configure:
     - **Environment**: Java
     - **Build Command**: `mvn clean package -DskipTests`
     - **Start Command**: `java -jar target/controle-financeiro-api-1.0.0.jar`
     - **Plan**: Free

3. **Configurar variáveis de ambiente**:
   - `SPRING_PROFILE=prod`
   - `PORT=10000` (ou deixar Render definir)
   - Link o PostgreSQL Database (Render criará automaticamente `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`)

4. **Deploy automático** a cada push no repositório!

📚 **Guia completo**: Veja [GUIA_DEPLOY_RENDER.md](GUIA_DEPLOY_RENDER.md) para instruções detalhadas.

### Variáveis de Ambiente Necessárias

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `SPRING_PROFILE` | Profile ativo (prod) | `prod` |
| `DATABASE_URL` | URL do banco PostgreSQL | `jdbc:postgresql://host:5432/db` |
| `DATABASE_USERNAME` | Usuário do banco | `postgres` |
| `DATABASE_PASSWORD` | Senha do banco | `sua-senha` |
| `PORT` | Porta da aplicação (opcional) | `8080` |

## 🎓 Conceitos de POO Aplicados

### 1. **Encapsulamento**
- Atributos privados nas entidades (`Transacao`, `Categoria`)
- Uso de getters/setters via Lombok
- DTOs expõem apenas dados necessários

### 2. **Herança**
- Exceções customizadas herdam de `RuntimeException`
- `ResourceNotFoundException` e `BusinessException`

### 3. **Polimorfismo**
- Interface `JpaRepository` com diferentes implementações
- Tratamento de diferentes tipos de exceções no `GlobalExceptionHandler`
- Enum `TipoTransacao` com comportamentos específicos

### 4. **Abstração**
- Interfaces de repositório (Spring Data JPA)
- Camada de serviço abstrai lógica de negócio
- DTOs abstraem detalhes das entidades

### 5. **Injeção de Dependências**
- Uso de construtor para injeção (baixo acoplamento)
- Inversão de controle gerenciada pelo Spring
- `@Autowired` implícito via construtor

## 📊 Modelo de Dados

### Entidade: Transacao
```java
- id: Long
- descricao: String
- valor: BigDecimal
- tipo: TipoTransacao (RECEITA/DESPESA)
- data: LocalDate
- categoria: Categoria (@ManyToOne)
- observacoes: String
- criadoEm: LocalDateTime
- atualizadoEm: LocalDateTime
```

### Entidade: Categoria
```java
- id: Long
- nome: String
- descricao: String
- tipo: TipoTransacao
- cor: String
- transacoes: List<Transacao> (@OneToMany)
```

### Relacionamentos
- `Transacao` **@ManyToOne** `Categoria`
- `Categoria` **@OneToMany** `Transacao`

## 👥 Divisão de Tarefas do Grupo

| Membro | Responsabilidade | Arquivos |
|--------|-----------------|----------|
| **Nikolas** | Configuração inicial e entidades | pom.xml, Transacao, Categoria, TipoTransacao |
| **Eduardo** | Repositórios e camada de serviço | TransacaoRepository, CategoriaRepository, Services |
| **Nikolas** | Controllers e documentação Swagger | TransacaoController, CategoriaController |
| **Marcela** | DTOs, validações e resumos | DTOs, ResumoFinanceiroDTO, validações |
| **Ezequiel** | Exceções e testes unitários | Exception classes, TransacaoServiceTest, ControllerTest |
| **Gustavo** | Configurações e deploy | application.properties, Dockerfile, README, data.sql |

## 📝 Boas Práticas Implementadas

- ✅ Arquitetura em camadas (MVC)
- ✅ Injeção de dependências via construtor
- ✅ Validação de entrada com Bean Validation
- ✅ Tratamento centralizado de exceções
- ✅ Uso de DTOs para separar modelo de domínio da API
- ✅ Testes unitários com alta cobertura (90%+)
- ✅ Documentação automática com Swagger
- ✅ Profiles para diferentes ambientes
- ✅ Variáveis de ambiente para dados sensíveis
- ✅ Mensagens de commit descritivas
- ✅ BigDecimal para valores monetários
- ✅ LocalDate e LocalDateTime para datas

## 🌟 Diferenciais do Projeto

1. **Integração Frontend-Backend**: API preparada para conectar com o frontend existente
2. **Cálculos Financeiros**: Lógica robusta para somar receitas, despesas e calcular saldo
3. **Queries Customizadas**: JPQL para cálculos agregados no banco de dados
4. **Enum TipoTransacao**: Type-safe para categorização
5. **Cores nas Categorias**: Suporte a visualização colorida no frontend
6. **Resumo Financeiro**: Endpoint especializado para dashboards
7. **Filtros Avançados**: Busca por período, tipo, categoria e descrição
8. **Validações Rigorosas**: @PastOrPresent para datas, @DecimalMin para valores
9. **Dados de Exemplo**: Script SQL com categorias e transações pré-cadastradas
10. **Pronto para Produção**: Dockerfile, Procfile e configurações completas

## 📚 Documentação Adicional

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Springdoc OpenAPI](https://springdoc.org/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'feat: Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT.

## ✉️ Contato

Para dúvidas ou sugestões:
- Email: contato@financeiro.com
- GitHub: https://github.com/seu-usuario/controle-financeiro-api

---

**💰 Desenvolvido com ❤️ como projeto acadêmico de Spring Boot**

**🎓 Microserviço de Controle Financeiro Pessoal**
# Controle-FinanceiroFront-
