package com.digitalMarket.productservice.unit.domain;

import com.digitalMarket.productservice.application.service.ProductService;
import com.digitalMarket.productservice.domain.exception.DomainValidationException;
import com.digitalMarket.productservice.domain.model.Product;
import com.digitalMarket.productservice.domain.port.output.ProductRepositoryPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("testcontainers")
@ExtendWith(MockitoExtension.class)
public class ProductDomain {

    @Mock
    private ProductRepositoryPort repository;

    @InjectMocks
    private ProductService service;

    @Test
    @DisplayName("Entidad Product se cree correctamente con reglas válidas")
    public void shouldCreateProductSuccessfully(){
            // Arrange

            String nombre = "Pruebas Unitarias con JUnit y Mockito";
            String descripcion = "Ebook 400 paginas libro digital";
            BigDecimal precio = new BigDecimal("70000");
            String tipoProducto = "Ebook";
            Long usuarioId = 1L;

            Product.create(
                    nombre,
                    descripcion,
                    precio,
                    tipoProducto,
                    usuarioId
            );

            Product savedProduct = Product.create(
                    nombre,
                    descripcion,
                    precio,
                    tipoProducto,
                    usuarioId
            );


            when(repository.save(any(Product.class)))
                    .thenReturn(savedProduct);

            // Act
            Product result = service.create(
                    nombre,
                    descripcion,
                    precio,
                    tipoProducto,
                    usuarioId
            );

            // Assert
            Assertions.assertThat(result).isNotNull();

            Assertions.assertThat(result.getNombre())
                        .isEqualTo(nombre);

            Assertions.assertThat(result.getDescripcion())
                        .isEqualTo(descripcion);

            Assertions.assertThat(result.getPrecio())
                        .isEqualByComparingTo(precio);

            Assertions.assertThat(result.getTipoProducto())
                        .isEqualTo(tipoProducto);

            // Verify Behavior Pattern

            verify(repository).save(any(Product.class));

            verifyNoMoreInteractions(repository);
    }


    @Test
    @DisplayName("Debe lanzar excepción cuando el precio es negativo")
    void shouldThrowExceptionWhenPriceIsNegative() {

        // Arrange
        BigDecimal invalidPrice = BigDecimal.valueOf(-100);

        // Act & Assert
        assertThrows(
                DomainValidationException.class,
                () -> new Product(
                        1L,
                        "Pruebas Unitarias con JUnit y Mockito",
                        "Ebook 400 paginas libro digital",
                        invalidPrice,
                        "Ebook",
                        true,
                        LocalDateTime.now(),
                        1L
                )
        );
    }

    @Test
    @DisplayName("El método desactivar() cambie el estado del producto")
    void shouldDeactivateProduct() {

        Product product = new Product(
                1L,
                "Pruebas Unitarias con JUnit y Mockito",
                "Ebook 400 paginas libro digital",
                new BigDecimal("70000"),
                "Ebook",
                true,
                LocalDateTime.now(),
                1L
        );

        product.desactivar();

        assertFalse(product.isActivo());
    }
}