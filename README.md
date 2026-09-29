# E-commerce API 🛒

API REST de catálogo de produtos e categorias, desenvolvida em **Java e Spring Boot** como projeto de evolução em desenvolvimento backend.

> **Status:** em desenvolvimento. As funcionalidades descritas abaixo refletem o código versionado; a execução e a suíte de testes ainda precisam ser validadas no ambiente de cada pessoa.

## Tecnologias

- **Java 25** (versão configurada no `pom.xml`)
- **Spring Boot 4.1.1**, Spring Web MVC, Spring Data JPA e Bean Validation
- **PostgreSQL 17** com Docker Compose
- **Maven Wrapper** para compilação e execução
- Testes com a infraestrutura de teste do Spring Boot, JUnit e Mockito

## Funcionalidades implementadas no código

- CRUD de **produtos** e **categorias**
- Associação de produtos a categorias
- DTOs e validação das requisições
- Pesquisa de produtos por nome, categoria e intervalo de preço
- Paginação e ordenação dos resultados
- Tratamento centralizado de exceções
- Testes automatizados de serviço e controller para produtos

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
| DELETE | `/api/categories/{id}` | Remove uma categoria |

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
    └── service/
```

## Executar localmente

**Pré-requisitos:** JDK 25 e Docker com Docker Compose. O `docker-compose.yml` inicia **somente o PostgreSQL**; a aplicação Spring Boot deve ser iniciada separadamente.

1. Clone o projeto:

   ```bash
   git clone https://github.com/ericvpereira/ecommerce-springboot.git
   cd ecommerce-springboot
   ```

2. Confira a configuração local em `src/main/resources/application.properties`. O projeto atualmente utiliza `jdbc:postgresql://localhost:5432/ecommerce`, usuário `postgres` e senha local de desenvolvimento. **Altere/remova credenciais fixas antes de qualquer implantação pública.**

3. Inicie o banco de dados:

   ```bash
   docker compose up -d
   ```

4. Execute a aplicação (Windows):

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   No Linux/macOS, use `./mvnw spring-boot:run`.

5. Para executar os testes localmente (Windows):

   ```powershell
   .\mvnw.cmd test
   ```

   No Linux/macOS, use `./mvnw test`. A execução dos testes depende das configurações de banco e do ambiente de testes; não há resultado de CI validado neste README.

## Próximos passos

- Externalizar as credenciais do banco de dados
- Ampliar a cobertura de testes e validar a execução automatizada
- Adicionar migrações de banco e documentação OpenAPI, conforme a evolução do projeto
- Evoluir o domínio para novas funcionalidades de e-commerce

---

**Autor:** [Eric Vieira](https://github.com/ericvpereira) · [Projeto Phonebook API](https://github.com/ericvpereira/springboot-phonebook)
