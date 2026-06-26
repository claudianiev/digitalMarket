package com.digitalMarket.apiGateway.bean;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;

@Configuration
@AllArgsConstructor
public class GatewayBeans {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
            .routes()
                // PRODUCT-SERVICE
                .route("product-service", r -> r
                        .path("/api/v1/products/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://PRODUCT-SERVICE"))

                // IAM-SERVICE USERS
                .route("iam-users-service", r -> r
                        .path("/api/v1/users/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://IAM-SERVICE"))

                // IAM-SERVICE ROLES
                .route("iam-role-service", r -> r
                        .path("/api/v1/role/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://IAM-SERVICE"))

                // IAM-SERVICE PERMITS
                .route("iam-permits-service", r -> r
                        .path("/api/v1/permits/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://IAM-SERVICE"))

                // IAM-SERVICE AUTH
                .route("iam-auth-service", r -> r
                        .path("/api/v1/auth/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://IAM-SERVICE"))
            .build();
    }
}