# Product API

A Spring Boot REST API for managing products and items, with JWT authentication, role-based authorization, HTTPS, and Docker support.

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- MySQL
- H2 (tests)
- JUnit 5 & Mockito
- Swagger / OpenAPI
- Maven
- Docker & Docker Compose

## Features

- Product & Item CRUD APIs
- JWT authentication
- Access & refresh tokens with rotation
- Role-based authorization (`USER`, `ADMIN`)
- Request validation & global exception handling
- Pagination
- CORS configuration
- HTTPS support
- Unit & integration testing
- Dockerized application

## Prerequisites

| Run method | You need |
|---|---|
| Docker (recommended) | [Docker Desktop](https://www.docker.com/products/docker-desktop/) and Git |
| Locally | Java 21, Maven (or the included `./mvnw`), and MySQL |

## Running with Docker (recommended)

This starts both the API and a MySQL database. You don't need Java or MySQL installed.

1. Clone the repository:

   ```bash
   git clone https://github.com/<your-username>/<repo-name>.git
   cd <repo-name>
   ```

2. Create your environment file:

   ```bash
   cp .env.example .env
   ```

   Open `.env` and fill in the values.

3. Start the app and MySQL:

   ```bash
   docker compose up --build
   ```

4. Open Swagger UI at:

   ```text
   https://localhost:8443/swagger-ui/index.html
   ```

   Your browser will show a certificate warning because the certificate is self-signed. Choose to proceed.

To stop the containers:

```bash
docker compose down
```

To stop and also delete the database data:

```bash
docker compose down -v
```

## Running Locally

1. Install Java 21 and MySQL, and create a database named `demo1`.
2. Set your database credentials, either by editing `src/main/resources/application.yml` or by setting these environment variables:

   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
   - `JWT_SECRET`
   - `SSL_KEYSTORE_PASSWORD`

3. Run the app:

   ```bash
   ./mvnw spring-boot:run
   ```

The API runs on:

```text
https://localhost:8443
```

## API Documentation

Swagger UI:

```text
https://localhost:8443/swagger-ui/index.html
```

## Getting Started with the API

The database starts empty, and most endpoints are protected by JWT. To try the API:

1. Register a user with the register endpoint.
2. Log in with the login endpoint to receive an access token and a refresh token.
3. In Swagger UI, click **Authorize** and paste the access token, or send it as a header:

   ```text
   Authorization: Bearer <your-access-token>
   ```

## Configuration

Docker Compose reads these values from your `.env` file:

| Variable | Description |
|---|---|
| `DB_PASSWORD` | MySQL root password |
| `JWT_SECRET` | Secret used to sign JWTs (use at least 32 characters) |
| `SSL_KEYSTORE_PASSWORD` | Password of the `product-api.p12` keystore |

Never commit your `.env` file. Only `.env.example` (with placeholder values) belongs in the repository.

## Testing

Run all tests:

```bash
./mvnw test
```

## Project Status

🚧 Actively developed. Testing and additional improvements are being implemented incrementally.
