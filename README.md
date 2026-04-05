# 💧 Consumo de Água API

API desenvolvida em **Kotlin + Spring Boot**, com arquitetura baseada em **Clean Architecture (DDD)**, para gerenciamento de gastos de consumo de água por usuário.

---

## 🚀 Tecnologias utilizadas

* Kotlin
* Spring Boot
* Spring Security (JWT)
* PostgreSQL
* JPA / Hibernate
* Maven

---

## 🧱 Arquitetura

O projeto segue uma organização baseada em **Clean Architecture**, separando responsabilidades:

```
config/
core/
  ├── entity/
  ├── exception/
  ├── port/
  └── usecase/
infra/
  ├── entrypoint/
  ├── gateway/
  └── security/
```

---

## 🔐 Autenticação

A autenticação é feita via **JWT (JSON Web Token)**.

### 📌 Login

```http
POST /auth/login
```

### Request:

```json
{
  "email": "usuario@email.com",
  "password": "123456"
}
```

### Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "type": "Bearer"
}
```

---

## 🔒 Uso do Token

Após autenticar, envie o token no header:

```
Authorization: Bearer SEU_TOKEN
```

---

## 📊 Endpoints

### 🔹 Criar usuário

```http
POST /user
```

---

### 🔹 Login

```http
POST /auth/login
```

---

### 🔹 Registrar gasto de água

```http
POST /water-expenses
```

---

### 🔹 Buscar gastos por usuário

```http
GET /water-expenses/user/{userId}
```

---

## 📌 Estrutura do gasto de água

Exemplo de payload:

```json
{
  "userId": 1,
  "referenceDate": "2025-10-01",
  "dueDate": "2025-10-15",
  "totalAmount": 80.50,
  "consumptionM3": 15,
  "waterAmount": 45.00,
  "sewageAmount": 35.50,
  "meterReading": 1245,
  "isPaid": false,
  "note": "Aumento devido à lavagem do quintal"
}
```

---

## ⚙️ Configuração

### application.yml

```yaml
security:
  jwt:
    secret: sua-chave-super-segura
    expiration: 86400000
```

---

## 🧪 Testes

Recomenda-se utilizar:

* Postman
* Insomnia

Fluxo:

1. Criar usuário
2. Fazer login
3. Copiar token
4. Usar Bearer Token nos endpoints protegidos

---

## 🚧 Melhorias futuras

* [ ] Remover `userId` do request e usar usuário do token
* [ ] Validações com Bean Validation
* [ ] Tratamento global de exceções (ControllerAdvice)
* [ ] Relatórios e métricas de consumo
* [ ] Integração com frontend/mobile

---

## 📱 Próximos passos

* Criar frontend web (HTML/JS)
* Adaptar API para uso mobile (Android/iOS)

---

## 👨‍💻 Autor

Desenvolvido por **Maikon Sposito**

---

## 📌 Objetivo

Projeto criado com foco em:

* Aprendizado de Kotlin
* Prática de arquitetura limpa
* Implementação de autenticação JWT
* Preparação para aplicações reais (mobile/web)

---
