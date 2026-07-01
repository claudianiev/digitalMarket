# Especificación Técnica — DigitalMarket

## 1. Resumen del proyecto

DigitalMarket es un backend construido con **arquitectura de microservicios** sobre **Spring Boot**, orientado a un marketplace digital. El sistema separa responsabilidades en servicios independientes que se comunican entre sí mediante un API Gateway y un servidor de descubrimiento (Eureka).

## 2. Arquitectura general

```
                         ┌────────────────-─┐
                         │     Cliente      │
                         └────────┬─────────┘
                                  │
                         ┌────────▼─────────┐
                         │   API Gateway    │  (Spring Cloud Gateway)
                         └────────┬─────────┘
                                  │
                 ┌────────────────┼────────────────┐
                 │                                 │
        ┌────────▼────────┐                ┌──────────▼─────────┐
        │   IAM Service     │              │  Product Service    │
        │ (Auth + Usuarios) │              │   (Catálogo)         │
        │  puerto interno 8081             │  puerto interno 8082 │
        └────────┬─────--───┘              └──────────┬─────────┘
                 │                                  │
                 └────────────────┬─────────────────┘
                                  │
                         ┌────────▼─────────┐
                         │   PostgreSQL 15   │
                         │  (digitalMarket)  │
                         └───────────────────┘

        ┌───────────────────────────────────┐
        │  Eureka Server (discoveryService) │  ← registro y descubrimiento
        └───────────────────────────────────┘
```

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
- **Base de datos:** PostgreSQL 15.2 (contenedor `digitalMarket_db`) fuente de verdad actual.
- **Contenerización:** Docker / Docker Compose
- **Build:** *(pendiente confirmar — probablemente Maven, a validar con el `pom.xml` de cada servicio)*

## 6. Modelo de datos

*Pendiente.* Para documentar las entidades (usuarios, roles, productos, categorías, etc.) es necesario revisar:
- `iamService/db/sql/digitalMarket.sql`
- Las clases `@Entity` de `iamService` y `productService`

## 7. Endpoints / Contratos de API

*Pendiente.* Para completar esta sección se necesita:
- Los `@RestController` de `iamService` y `productService`, o
- Una colección Postman / especificación OpenAPI, si existe

## 8. Seguridad

- Autenticación basada en **JWT**, gestionada por `iamService` a través de **Spring Security**.
- *Pendiente confirmar:* flujo exacto de emisión/validación de tokens, manejo de refresh tokens, roles y permisos, y si el API Gateway valida el JWT antes de enrutar o delega esa validación a cada microservicio.


