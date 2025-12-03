# JWT Validator API

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.0-brightgreen)
![Gradle](https://img.shields.io/badge/Gradle-8+-blue)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-purple)
![Docker](https://img.shields.io/badge/Docker-Supported-2496ED?logo=docker)

**🇬🇧 English | [🇧🇷 Português](#pt-doc)**

---
<a id="en-doc"></a>
# EN – Documentation in English

## 🚀 Overview

This project exposes a simple HTTP API that validates a JWT according to a set of specific business rules.  
The focus was on clarity, maintainability, Hexagonal Architecture, automated tests, and optional containerization.

## 🧩 Requirements

- Java 25+
- Gradle 8+
- Docker (optional)

---

## 📌 Validation Rules

A token is considered **valid** only if all criteria below are satisfied:

### 📍 Claims
The JWT payload must contain **exactly 3 claims**:

| Claim  | Rule |
|--------|------|
| **Name** | Must contain no digits and be at most 256 characters |
| **Role** | Must be one of: `"Admin"`, `"Member"`, `"External"` |
| **Seed** | Must be a prime number |

### 📍 Example Payloads

#### Valid payload
```
{
  "Name": "Maria Silva",
  "Role": "Admin",
  "Seed": 13
}
```

#### Invalid payload
```
{
  "Name": "Jose123",
  "Role": "SuperAdmin",
  "Seed": 12
}
```

### 📍 Token structure

- Must be a valid JWT with **three Base64URL parts**
- Payload must decode to valid JSON
- Extra claims invalidate the token
- Empty or null token → automatically **false**

---

## 📝 Assumptions

- The challenge does not require signature verification.
- Only 3 claims are allowed; extra claims invalidate the token.
- Response must be plain text (“verdadeiro” / “falso”).
- Token is passed via query parameter, not request body.

---

## 🧱 Architecture (Hexagonal)

                +-----------------------------+
                |        Controller (web)     |
                |       infra/in/web          |
                +---------------+-------------+
                                |
                      (Input Port)
                                |
                 +--------------+-------------+
                 |      Domain / Use Case      |
                 |  JwtValidationUseCase       |
                 +--------------+-------------+
                                |
                      (Output Port)
                                |
           +--------------------+--------------------+
           |    JWT Decoder Adapter (Jackson)        |
           |       infra/out/jwt                     |
           +-----------------------------------------+

**Why Hexagonal?**

- Domain independent from Spring
- Easy to test (pure unit tests)
- Zero framework coupling in the core
- Easy to replace adapters if needed

---

## 🛠️ How to Run Locally

### ▶️ Using Gradle

```
./gradlew bootRun
```

Server runs at:
http://localhost:8080

### ▶️ Build JAR manually
```
./gradlew clean bootJar
```

### ▶️ Run JAR
```
java -jar build/libs/jwtvalidator-0.0.1-SNAPSHOT.ja
```

---

### 🐳 Docker
```
1. Build image
docker build -t jwt-validator .

2. Run container
docker run --rm -p 8080:8080 jwt-validator
```

## 📘 API Specification
```
GET /api/v1/jwt/validate?token=<JWT>
```

### Responses
```
200 OK  
Body: "verdadeiro" or "falso"  
Content-Type: text/plain
```


✔️ Valid token example response:
```
verdadeiro
```

❌ Invalid token:
```
falso
```
### 🧪 Tests

Run all tests:
```
./gradlew test
```

Tests include:
 - JWT decoding tests
 - Prime number validation
 - Controller tests using Mockito
 - Validation rule scenarios

### 📬 Postman Collection

Available at:
[Postman Collection](postman)

Includes:
 - Valid sample token
 - Invalid JWT
 - Name with digits
 - Extra claims
 - Empty token

---

## 🧠 Prompt Engineering Usage

ChatGPT was used for:
- Brainstorming architectural alternatives
- Generating testing ideas
- Refining documentation and commit messages
- Debugging strategies

---

## 📦 Project Structure

```
src/main/java/com/ewertonilima/
├── config/
│   └── BeanConfig.java
├── domain/
│   ├── JwtValidationUseCase.java
│   └── port/
│       └── JwtDecoderPort.java
└── infra/
    ├── in/
    │   └── web/
    │       └── JwtValidationController.java
    └── out/
        └── jwt/
            └── JacksonJwtDecoderAdapter.java
```

---

## 🔮 Future Work (not implemented due to time)

- Docker Compose
- Helm Chart (Kubernetes)
- Terraform provisioning on AWS
- CI/CD with GitHub Actions
- AWS deployment (ECS/Fargate, API Gateway or ALB)
- Observability (Prometheus/Grafana)

---

**🇧🇷 Português | [🇬🇧 English](#en-doc)**

<a id="pt-doc"></a>
# PT – Documentação em Português

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.0-brightgreen)
![Gradle](https://img.shields.io/badge/Gradle-8+-blue)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-purple)
![Docker](https://img.shields.io/badge/Docker-Supported-2496ED?logo=docker)

## 🚀 Visão Geral

Este projeto expõe uma API HTTP simples para validar um JWT conforme regras específicas.
O foco foi clareza, arquitetura hexagonal, testes automatizados e containerização opcional.

## 🧩 Requisitos

- Java 25+
- Gradle 8+
- Docker (opcional)

---

## 📌 Regras de Validação

O token é válido apenas se todas as regras forem atendidas:

### 📍 Claims
O payload JWT deve conter **exatamente 3 claims**:

| Claim  | Regra |
|--------|--|
| **Name** | Não pode ter números e deve ter até 256 caracteres|
| **Role** | Deve ser: "Admin", "Member", "External"|
| **Seed** | Deve ser um número primo|

### 📍 Exemplos de Payload

#### Payload válido
```
{
  "Name": "Maria Silva",
  "Role": "Admin",
  "Seed": 13
}
```

#### Payload inválido
```
{
  "Name": "Jose123",
  "Role": "SuperAdmin",
  "Seed": 12
}
```

### 📍 Estrutura do Token

- Token deve ter **três partes Base64URL**
- Payload deve ser JSON válido
- Claims extras tornam o token inválido
- Token nulo ou vazio → **falso**

---

## 📝 Premissas

- O desafio não exige verificação da assinatura.
- Apenas 3 claims são permitidos; claims extras invalidam o token.
- A resposta deve ser texto puro (“verdadeiro” / “falso”).
- O token é passado via query parameter, não no corpo da requisição.

---

## 🧱 Arquitetura (Hexagonal)

                          +----------------------------------+
                          |        HTTP Controller (Web)     |
                          |           infra/in/web           |
                          +----------------+-----------------+
                                           |
                                           |  (Input Port)
                                           |
                         +-----------------+------------------+
                         |        Domínio / Regra de Negócio |
                         |       JwtValidationUseCase         |
                         |           (Java puro)              |
                         +-----------------+------------------+
                                           |
                                           |  (Output Port)
                                           |
             +-----------------------------+-------------------------------+
             |     Adaptador de Decodificação JWT (Jackson)                |
             |                infra/out/jwt                               |
             |     Implementa JwtDecoderPort sem acoplamento no domínio   |
             +-------------------------------------------------------------+

**Por que hexagonal?**

- Domínio independente do Spring
- Fácil de testar (testes unitários puros)
- Zero acoplamento com frameworks no núcleo
- Fácil de substituir adaptadores, se necessário

---

## 🛠️  Como Executar Localmente

### ▶️ Via Gradle

```
./gradlew bootRun
```

A API ficará disponível em:
http://localhost:8080

### ▶️ Gerar JAR manualmente
```
./gradlew clean bootJar
```

### ▶️ Executar JAR
```
java -jar build/libs/jwtvalidator-0.0.1-SNAPSHOT.jar
```

---

### 🐳 Executando com Docker
```
1. Construir imagem
docker build -t jwt-validator .

2. Rodar container
docker run --rm -p 8080:8080 jwt-validator
```

## 📘 Especificação da API
```
GET /api/v1/jwt/validate?token=<JWT>
```

### Respostas
```
200 OK  
Corpo: "verdadeiro" ou "falso"  
Content-Type: text/plain
```
✔️ Exemplo de resposta com token válido:
```
verdadeiro
```

❌ Exemplo de resposta com token inválido:

```
falso
```
### 🧪 Testes


Rodar todos:
```
./gradlew test
```

Inclui:
- Testes do decoder JWT
- Teste de números primos
- Teste do controller com Mockito
- Casos completos de validação

### 📬 Collection do Postman

Arquivo:
[Postman Collection](postman)

Inclui cenários:
- Token válido
- JWT inválido
- Name com números
- Claims extras
- Token vazio

---

## 🧠 Uso de Engenharia de Prompt

ChatGPT foi utilizado como apoio para:
- Exploração arquitetural
- Melhoria de README
- Refinamento de commits
- Validação de casos extremos
- Ideias de testes

---

## 📦 Estrutura do Projeto

```
src/main/java/com/ewertonilima/
├── config/
│   └── BeanConfig.java
├── domain/
│   ├── JwtValidationUseCase.java
│   └── port/
│       └── JwtDecoderPort.java
└── infra/
    ├── in/
    │   └── web/
    │       └── JwtValidationController.java
    └── out/
        └── jwt/
            └── JacksonJwtDecoderAdapter.java
```
---

## 🔮 Melhorias Futuras (não implementado devido ao tempo)

- Docker Compose
- Helm Chart (Kubernetes)
- Provisionamento com Terraform na AWS
- CI/CD com GitHub Actions
- Implantação na AWS (ECS/Fargate, API Gateway ou ALB)
- Observabilidade (Prometheus/Grafana)
