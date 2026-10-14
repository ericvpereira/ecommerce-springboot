# E-commerce API 🛒

API REST de catálogo de produtos e categorias, desenvolvida em **Java e Spring Boot** como projeto de evolução em desenvolvimento backend.

> **Status:** em desenvolvimento. A versão atual possui CRUD de produtos e categorias, filtros, paginação, validações, Flyway, OpenAPI/Swagger e uma suíte de **43 testes automatizados**, validada localmente.

## Tecnologias

- **Java 25** (versão configurada no `pom.xml`)
- **Spring Boot 4.1.1**, Spring Web MVC, Spring Data JPA e Bean Validation
- **PostgreSQL 17** com Docker Compose
- **Maven Wrapper** para compilação e execução
- **Flyway** para versionamento do schema do banco
- **OpenAPI/Swagger (springdoc)** para documentação interativa da API
- **Testcontainers** para testes de integração com PostgreSQL
- Testes com Spring Boot Test, JUnit e Mockito

## Funcionalidades implementadas no código

- CRUD de **produtos** e **categorias**
- Associação de produtos a categorias
- DTOs e validação das requisições
- Pesquisa de produtos por nome, categoria e intervalo de preço
- Paginação e ordenação dos resultados
- Tratamento centralizado de exceções
- Regra de integridade que impede excluir uma categoria enquanto existirem produtos associados
- Migração inicial de banco gerenciada pelo Flyway
- Documentação OpenAPI/Swagger dos endpoints e respostas de erro
- Testes automatizados de controller, service, specifications e integração

## Principais endpoints

### Produtos

| Método | Endpoint | Descrição |
| --- | --- | --- |
| GET | `/api/products` | Lista produtos, com filtros opcionais e paginação |
| GET | `/api/products/{id}` | Busca um produto |
| POST | `/api/products` | Cadastra um produto |
| PUT | `/api/products/{id}` | Atualiza um produto |
| DELETE | `/api/products/{id}` | Remove um produto |

**Filtros opcionais** no GET `/api/products`: `nome`, `categoriaId`, `precoMin`, `precoMax`, além dos parâmetros de paginação e ordenação do Spring Data. Para filtrar por preço, envie **ambos** `precoMin` e `precoMax`.

Exemplo: `GET /api/products?nome=notebook&precoMin=1000&precoMax=5000&page=0&size=10`

Exemplo de corpo para criar um produto (a categoria precisa existir):

```json
{
  "nome": "Notebook",
  "preco": 3500.00,
  "categoriaId": 1
}
```

### Categorias

| Método | Endpoint | Descrição |
| --- | --- | --- |
| GET | `/api/categories` | Lista categorias |
| GET | `/api/categories/{id}` | Busca uma categoria |
| POST | `/api/categories` | Cadastra uma categoria |
| PUT | `/api/categories/{id}` | Atualiza uma categoria |
| DELETE | `/api/categories/{id}` | Remove uma categoria somente se ela não estiver em uso |

### Regra de exclusão de categorias

Ao tentar excluir uma categoria que possui produtos associados, a API mantém a integridade dos dados e responde com **HTTP 409 Conflict**.

Exemplo de resposta:

```json
{
  "status": 409,
  "message": "Categoria em uso",
  "errors": {
    "categoria": "A categoria não pode ser excluída porque possui produtos associados"
  }
}
```

## Estrutura do projeto

```text
src/
├── main/
│   ├── java/com/eric/ecommerce/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exceptions/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   └── specification/
│   └── resources/application.properties
└── test/java/com/eric/ecommerce/
    ├── controller/
    ├── repository/
    └── service/
```

## Executar localmente

**Pré-requisitos:** JDK 25 e Docker com Docker Compose. O `docker-compose.yml` inicia **somente o PostgreSQL**; a aplicação Spring Boot deve ser iniciada separadamente.

1. Clone o projeto:

   ```bash
   git clone https://github.com/ericvpereira/ecommerce-springboot.git
   cd ecommerce-springboot
   ```

2. Configure as variáveis de ambiente usadas pela aplicação:

   **PowerShell (Windows):**

   ```powershell
   $env:DB_URL="jdbc:postgresql://localhost:5432/ecommerce"
   $env:DB_USERNAME="postgres"
   $env:DB_PASSWORD="postgres"
   ```

   O `application.properties` lê essas configurações por meio de `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`, evitando credenciais fixas no código.

3. Inicie o banco de dados:

   ```bash
   docker compose up -d
   ```

4. Execute a aplicação (Windows):

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   No Linux/macOS, use `./mvnw spring-boot:run`.

5. Com a aplicação em execução, a documentação interativa fica disponível em:

   - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
   - OpenAPI JSON: `http://localhost:8080/v3/api-docs`

6. Para executar os testes localmente (Windows):

   ```powershell
   .\mvnw.cmd test
   ```

   No Linux/macOS, use `./mvnw test`. A suíte atual possui **43 testes** entre testes unitários, de controller, specifications e integração. Os testes de integração utilizam Testcontainers com PostgreSQL.

## Próximos passos

- Adicionar integração contínua (CI) para validar build e testes automaticamente
- Implementar autenticação e autorização com Spring Security/JWT
- Evoluir o domínio para carrinho, pedidos, estoque e regras transacionais
- Continuar ampliando a cobertura de testes conforme novas funcionalidades forem adicionadas

---

**Autor:** [Eric Vieira](https://github.com/ericvpereira) · [Projeto Phonebook API](https://github.com/ericvpereira/springboot-phonebook)
