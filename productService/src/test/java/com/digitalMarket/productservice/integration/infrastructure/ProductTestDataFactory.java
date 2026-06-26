package com.digitalMarket.productservice.integration.infrastructure;

import com.digitalMarket.productservice.domain.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductTestDataFactory {
    private ProductTestDataFactory() {
    }

    public static Product createValidProduct() {

        return Product.restore(
                null,
                "Curso Spring Boot",
                "Curso backend avanzado",
                new BigDecimal("120000"),
                "Curso",
                true,
                LocalDateTime.now(),
                1L
        );
    }

    public static Product createInactiveProduct() {

        return Product.restore(
                null,
                "Ebook Java",
                "Ebook arquitectura limpia",
                new BigDecimal("50000"),
                "Ebook",
                false,
                LocalDateTime.now(),
                2L
        );
    }
}
