# DigitalMarket

Backend desarrollado con arquitectura de microservicios usando Spring Boot.

## Tecnologías

- Java
- Spring Boot
- Spring Security
- JWT
- Spring Cloud Gateway
- Eureka
- MySQL
- Docker

## Microservicios

- API Gateway
- Eureka Server
- IAM Service
- Product Service

## Arquitectura general

                         ┌───────────────-──┐
                         │     Cliente      │
                         └────────┬─────────┘
                                  │
                         ┌────────▼─────────┐
                         │   API Gateway    │  (Spring Cloud Gateway)
                         └────────┬─────────┘
                                  │
                 ┌────────────────┼────────────────┐
                 │                                 │
        ┌────────▼────────┐             ┌──────────▼─────────┐
        │   IAM Service   │             │  Product Service   │
        │ (Auth + Usuarios)│            │   (Catálogo)       │
        │  puerto interno 8081          │ puerto interno 8082│
        └────────┬────────┘             └──────────┬─────────┘
                 │                                 │
                 └───────────────┬─────────────────┘
                                 │
                         ┌───────▼─────────┐
                         │  PostgreSQL 15  │
                         │ (digitalMarket) │
                         └─────────────────┘

        ┌───────────────────────────────────┐
        │  Eureka Server (discoveryService) │  ← registro y descubrimiento
        └───────────────────────────────────┘

        ## 3. Componentes / Microservicios
 
| Servicio | Carpeta | Responsabilidad | Puerto interno | Puerto expuesto (Docker) |
|---|---|---|---|---|
| **API Gateway** | `api-gateway` | Punto de entrada único, enrutamiento hacia microservicios | — *(pendiente confirmar)* | — *(pendiente confirmar)* |
| **Discovery Service** | `discoveryService` | Registro y descubrimiento de servicios (Eureka Server) | — *(pendiente confirmar)* | — *(pendiente confirmar)* |
| **IAM Service** | `iamService` | Autenticación, autorización, gestión de usuarios, emisión de JWT | 8081 | 8085 |
| **Product Service** | `productService` | Gestión del catálogo de productos | 8082 | 8086 |
 
## 4. Stack tecnológico
 
- **Lenguaje:** Java
- **Framework:** Spring Boot
- **Seguridad:** Spring Security + JWT
- **Gateway:** Spring Cloud Gateway
- **Descubrimiento de servicios:** Netflix Eureka
- **Base de datos:** PostgreSQL 15.2 (contenedor `digitalMarket_db`)
- **Contenerización:** Docker / Docker Compose
- **Build:** *(pendiente confirmar — probablemente Maven, a validar con el `pom.xml` de cada servicio)*
