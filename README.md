# E-Commerce Microservices

This project is a modular microservices architecture for an e-commerce application. It is based on Spring Boot 3.4.x, uses Java 21, and integrates various modern technologies such as Eureka, Spring Cloud Config, Kafka, Keycloak, and Zipkin for distributed tracing.

---

## Project Structure

The project is a multi-module Maven project with the following modules:

| Module         | Description                                           |
|----------------|-------------------------------------------------------|
| config-server  | Spring Cloud Config Server for centralized configuration of microservices |
| discovery      | Eureka Server for service discovery                   |
| gateway        | API Gateway with Spring Cloud Gateway                 |
| customer       | Customer management service                            |
| order          | Order management service                               |
| payment        | Payment processing service                             |
| product        | Product catalog service                                |
| notification   | Notification service (email, etc.)                     |

---

## Infrastructure (Docker Compose)

The infrastructure includes:

- **PostgreSQL** (version 14, persistent storage)
- **MongoDB** with Mongo Express UI
- **Kafka** & **Zookeeper** for messaging
- **Zipkin** for distributed tracing
- **MailDev** as a mail testing server
- **Keycloak** (version 26) for OAuth2 / OpenID Connect authentication
- **Network:** All containers are connected via `microservices-net`

---

## Technologies & Frameworks

- **Java 21**
- **Spring Boot 3.4.x**
- **Spring Cloud Config**
- **Spring Cloud Netflix Eureka**
- **Spring Cloud Gateway**
- **Spring Data JPA & MongoDB**
- **Flyway for database migrations**
- **Kafka messaging**
- **Keycloak for security / OAuth2**
- **Zipkin & Micrometer for tracing**
- **Lombok, Logback, SLF4J**

---

## Configuration

- All microservices load their configuration from the `config-server` running on port 8888.
- Ports and database connections are configured per service.
- Databases used:
    - PostgreSQL (JPA with Hibernate, with different `ddl-auto` settings)
    - MongoDB (for some services)
- Kafka topics are service-specific (e.g. `payment-topic` for the payment service).
- The API Gateway connects and routes requests to microservices via service discovery.
- Keycloak is integrated as the authentication and authorization server.
- MailDev acts as SMTP server for development and testing.

---

## Starting & Development

1. **Start Docker Compose stack**

   ```bash
   docker-compose up -d
