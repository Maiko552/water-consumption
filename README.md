# Consumo de Agua Kotlin

Aplicacao Spring Boot em Kotlin para cadastro de usuarios, autenticacao com JWT e controle de contas de agua.

## Tecnologias

- Kotlin
- Spring Boot
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- PostgreSQL

## Como rodar

Suba o banco com Docker:

```bash
docker compose up -d
```

Depois inicie a aplicacao:

```bash
mvn spring-boot:run
```

Configuracao padrao em [application.yaml](/C:/Users/user/.codex/worktrees/7a16/consumo-agua-kotlin/src/main/resources/application.yaml:1):

- Banco: `consumo_db`
- Porta do Postgres: `5433`
- Usuario: `postgres`
- Senha: `postgres`

## Fluxo de autenticacao

1. Crie um usuario em `POST /user`.
2. Faça login em `POST /auth/login`.
3. Use o token retornado no header `Authorization: Bearer <token>`.
4. As rotas de despesas usam sempre o usuario autenticado.

## Endpoints da API

### `POST /user`

Cria um novo usuario.

Exemplo de body:

```json
{
  "name": "Maria",
  "email": "maria@email.com",
  "password": "123456"
}
```

### `POST /auth/login`

Autentica o usuario e devolve um JWT.

Exemplo de body:

```json
{
  "email": "maria@email.com",
  "password": "123456"
}
```

### `POST /api/water-expenses`

Cria uma conta de agua para o usuario autenticado.

Importante:
- Nao envia `userId` no body.
- O backend identifica o usuario pelo token JWT.

Exemplo de body:

```json
{
  "referenceDate": "2026-04-01",
  "dueDate": "2026-04-10",
  "totalAmount": 145.90,
  "consumptionM3": 12.5,
  "waterAmount": 80.00,
  "sewageAmount": 65.90,
  "meterReading": 235.7,
  "isPaid": false,
  "note": "Conta do mes de abril"
}
```

### `GET /api/water-expenses`

Lista as contas do usuario autenticado.

Importante:
- Nao recebe `userId` na URL.
- O backend retorna apenas as despesas do usuario autenticado.

## Interface web

Rotas HTML com Thymeleaf:

- `GET /login`
- `GET /water-expenses`
- `GET /water-expenses/form`
- `POST /water-expenses/save`

O formulario web tambem nao envia mais `userId`; o backend usa o usuario autenticado.

## Testes

Os testes usam H2 em memoria por meio de [src/test/resources/application.yaml](/C:/Users/user/.codex/worktrees/7a16/consumo-agua-kotlin/src/test/resources/application.yaml:1), para nao depender do PostgreSQL local.

```bash
mvn test
```
