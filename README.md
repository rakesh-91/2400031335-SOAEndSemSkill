# JWT Authentication Microservices

This beginner-friendly Maven multi-module project contains exactly two Spring Boot services:

- **eureka-server** - Netflix Eureka registry and dashboard on port `9000`.
- **auth-service** - JWT authentication REST API on port `8081`, using H2 and Spring Data JPA.

The services use Java 17, Spring Boot 3.3.5, Spring Cloud 2023.0.3, Spring Security, JJWT, Springdoc OpenAPI, and Maven.

## Project structure

```text
jwt-auth-project/
├── pom.xml
├── eureka-server/
│   ├── pom.xml
│   └── src/main/java/com/example/eureka/EurekaServerApplication.java
└── auth-service/
    ├── pom.xml
    └── src/main/java/com/example/auth/
        ├── controller/
        ├── service/
        ├── repository/
        ├── entity/
        ├── dto/
        ├── security/
        ├── config/
        └── exception/
```

## How authentication works

Registration stores only a BCrypt hash in the in-memory H2 database. Login delegates credential checking to Spring Security and returns a signed JWT that expires after one hour. The JWT filter verifies the signature and expiry on every protected request, then places the authenticated user in Spring Security's context. Sessions are stateless.

## Configure the JWT secret safely

Set a secret of at least 32 bytes before starting the authentication service. The default value is only a local-development placeholder and must not be used in production.

PowerShell:

```powershell
$env:JWT_SECRET = "replace-with-a-long-random-secret-at-least-32-bytes"
$env:JWT_EXPIRATION = "3600000"
```

Do not commit `.env` files or real secrets; `.gitignore` already excludes them.

## Build and run

From the project root:

```powershell
mvn clean verify
```

Start Eureka Server in one terminal:

```powershell
mvn -pl eureka-server spring-boot:run
```

Start Authentication Service in another terminal (after setting `JWT_SECRET`):

```powershell
$env:JWT_SECRET = "replace-with-a-long-random-secret-at-least-32-bytes"
mvn -pl auth-service spring-boot:run
```

Eureka dashboard: http://localhost:9000  
Swagger UI: http://localhost:8081/swagger-ui/index.html  
OpenAPI JSON: http://localhost:8081/v3/api-docs

## Manual Swagger flow

1. Start Eureka Server.
2. Open http://localhost:9000.
3. Start Authentication Service.
4. Verify that `AUTH-SERVICE` is registered in Eureka.
5. Open http://localhost:8081/swagger-ui/index.html.
6. Call `POST /auth/register` with `{"username":"admin","password":"Admin@123"}`. It returns HTTP 201.
7. Call `POST /auth/login` with the registered credentials. It returns a Bearer token and `expiresIn: 3600`.
8. Copy the `token` value.
9. Click Swagger's **Authorize** button.
10. Enter the JWT token in the Bearer authentication field.
11. Call `GET /api/secure`; it returns HTTP 200 and the authenticated username.
12. Remove authorization and call the protected endpoint again; it returns HTTP 401.
13. Try invalid credentials, a modified token, and an expired token; each protected/authenticated request returns HTTP 401.

`GET /api/public` is available without a token. `GET /auth/validate` requires a valid Bearer token.

## Automated tests

Run all tests:

```powershell
mvn test
```

Tests use local mocks and an in-memory database where applicable; they do not require a running Eureka Server.

## Troubleshooting

- **401 from protected APIs:** ensure the header is exactly `Authorization: Bearer <token>` and that the token has not expired.
- **JWT secret error:** set `JWT_SECRET` to at least 32 bytes before starting `auth-service`.
- **AUTH-SERVICE is absent in Eureka:** start Eureka first and check that port `9000` is available.
- **Port already in use:** stop the process using port `9000` or `8081`, then restart the corresponding service.
- **Duplicate registration:** use a different username or restart `auth-service` to reset its in-memory H2 database.
