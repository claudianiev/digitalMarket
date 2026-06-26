package com.digitalMarket.productservice.unit.application;

import com.digitalMarket.productservice.application.dto.filter.ProductFilterRequest;
import com.digitalMarket.productservice.application.dto.request.ProductRequest;
import com.digitalMarket.productservice.application.dto.response.ProductResponse;
import com.digitalMarket.productservice.application.service.ProductService;
import com.digitalMarket.productservice.domain.exception.ProductNotFoundException;
import com.digitalMarket.productservice.domain.model.Product;
import com.digitalMarket.productservice.domain.port.output.ProductRepositoryPort;
import com.digitalMarket.productservice.infrastructure.adapter.persistence.mapper.ProductMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("testcontainers")
public class ProductApplication {

    private final ProductRepositoryPort repositoryPort = mock(ProductRepositoryPort.class);

    private final ProductService productService = new ProductService(repositoryPort);

    private final ProductMapper mapper = new ProductMapper();

    @Test
    @DisplayName("Que el caso de uso invoque correctamente el puerto de persistencia")
    public void shouldCallSavePort(){
        // Arrange
        when(repositoryPort.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        productService.create(
                "Pruebas Unitarias con JUnit y Mockito",
                "Ebook 400 paginas libro digital",
                new BigDecimal("70000"),
                "Ebook",
                1L
        );

        // Assert
        ArgumentCaptor<Product> productCaptor =
                ArgumentCaptor.forClass(Product.class);

        verify(repositoryPort, times(1))
                .save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();

        assertEquals(
                "Pruebas Unitarias con JUnit y Mockito",
                savedProduct.getNombre()
        );

        assertEquals(
                "Ebook 400 paginas libro digital",
                savedProduct.getDescripcion()
        );

        assertEquals(
                new BigDecimal("70000"),
                savedProduct.getPrecio()
        );

        assertEquals(
                "Ebook",
                savedProduct.getTipoProducto()
        );

        assertEquals(
                1L,
                savedProduct.getUsuarioId()
        );

        verifyNoMoreInteractions(repositoryPort);
    }

    @Test
    @DisplayName("Debe orquestar correctamente DTO -> dominio -> persistencia")
    void shouldCreateProductFromUseCase() {
        // Arrange
        ProductRequest request = new ProductRequest(
                "Curso Spring Boot",
                "Curso backend avanzado",
                new BigDecimal("120000"),
                "Curso",
                1L
        );

        when(repositoryPort.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        productService.create(
                request.nombre,
                request.descripcion,
                request.precio,
                request.tipoProducto,
                request.usuarioId
        );

        // Assert
        ArgumentCaptor<Product> productCaptor =
                ArgumentCaptor.forClass(Product.class);

        verify(repositoryPort, times(1))
                .save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();

        assertEquals(request.nombre, savedProduct.getNombre());
        assertEquals(request.descripcion, savedProduct.getDescripcion());
        assertEquals(request.precio, savedProduct.getPrecio());
        assertEquals(request.tipoProducto, savedProduct.getTipoProducto());
        assertEquals(request.usuarioId, savedProduct.getUsuarioId());

        verifyNoMoreInteractions(repositoryPort);
    }

    @Test
    @DisplayName("Debe retornar producto por id consultando correctamente el repositorio")
    void shouldReturnProductById() {

        // Arrange
        Long productId = 1L;

        Product product = new Product(
                1L,
                "Curso Spring Boot",
                "Curso backend avanzado",
                new BigDecimal("120000"),
                "Curso",
                true,
                LocalDateTime.now(),
                1L
        );

        when(repositoryPort.findById(productId))
                .thenReturn(Optional.of(product));

        // Act
        Product result = productService.getById(productId);

        // Assert - State Verification
        assertNotNull(result);

        assertEquals(productId, result.getId());

        assertEquals(
                "Curso Spring Boot",
                result.getNombre()
        );

        assertEquals(
                new BigDecimal("120000"),
                result.getPrecio()
        );

        // Assert - Verify Behavior Pattern
        verify(repositoryPort, times(1))
                .findById(productId);

        verifyNoMoreInteractions(repositoryPort);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el producto no existe")
    void shouldThrowExceptionWhenProductDoesNotExist() {

        // Arrange
        Long productId = 99L;

        when(repositoryPort.findById(productId))
                .thenReturn(Optional.empty());

        // Act + Assert - Exception Testing Pattern
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getById(productId)
        );

        assertEquals(
                "Producto con id " + productId + " no encontrado",
                exception.getMessage()
        );

        // Assert - Verify Behavior Pattern
        verify(repositoryPort, times(1))
                .findById(productId);

        verifyNoMoreInteractions(repositoryPort);
    }


    @ParameterizedTest
    @CsvSource({
            "Spring Boot,Curso,true,50000,150000,0,10",
            "Java,Ebook,true,10000,80000,1,5",
            "Angular,Plantilla,false,20000,90000,2,3"
    })
    @DisplayName("Debe retornar productos filtrados y paginados correctamente")
    void shouldReturnFilteredProductsPage(
            String nombre,
            String tipoProducto,
            Boolean activo,
            BigDecimal precioMin,
            BigDecimal precioMax,
            int page,
            int size
    ) {

        // Arrange
        ProductFilterRequest filter = new ProductFilterRequest(
                nombre,
                tipoProducto,
                activo,
                precioMin,
                precioMax,
                page,
                size
        );

        List<Product> products = List.of(
                new Product(
                        1L,
                        nombre,
                        "Descripcion producto",
                        new BigDecimal("70000"),
                        tipoProducto,
                        activo,
                        LocalDateTime.now(),
                        1L
                )
        );

        Page<Product> productPage = new PageImpl<>(
                products,
                PageRequest.of(page, size),
                products.size()
        );

        when(repositoryPort.search(filter))
                .thenReturn(productPage);

        // Act
        Page<Product> result = productService.execute(filter);

        // Assert - State Verification
        assertNotNull(result);

        assertFalse(result.isEmpty());

        assertEquals(1, result.getContent().size());

        assertEquals(page, result.getNumber());

        assertEquals(size, result.getSize());

        Product resultProduct = result.getContent().get(0);

        assertEquals(nombre, resultProduct.getNombre());

        assertEquals(tipoProducto, resultProduct.getTipoProducto());

        assertEquals(activo, resultProduct.isActivo());

        assertTrue(
                resultProduct.getPrecio().compareTo(precioMin) >= 0
        );

        assertTrue(
                resultProduct.getPrecio().compareTo(precioMax) <= 0
        );

        // Assert - Verify Behavior Pattern
        verify(repositoryPort, times(1))
                .search(filter);

        verifyNoMoreInteractions(repositoryPort);
    }

    @Test
    @DisplayName("Debe transformar correctamente ProductRequest a Product")
    void shouldMapRequestToDomain() {

        // Arrange
        ProductRequest request = new ProductRequest(
                "Curso Spring Boot",
                "Curso backend avanzado",
                new BigDecimal("120000"),
                "Curso",
                1L
        );

        // Act
        Product product = Product.create(
                request.nombre,
                request.descripcion,
                request.precio,
                request.tipoProducto,
                request.usuarioId
        );

        // Assert
        assertNotNull(product);

        assertEquals(
                request.nombre,
                product.getNombre()
        );

        assertEquals(
                request.descripcion,
                product.getDescripcion()
        );

        assertEquals(
                request.precio,
                product.getPrecio()
        );

        assertEquals(
                request.tipoProducto,
                product.getTipoProducto()
        );

        assertEquals(
                request.usuarioId,
                product.getUsuarioId()
        );

        assertTrue(product.isActivo());

        assertNotNull(product.getFechaCreacion());
    }


    @Test
    @DisplayName("Debe mapear correctamente Product hacia ProductResponse")
    void shouldMapDomainToResponse() {

        // Arrange - Test Builder Pattern
        Product product = ProductBuilder.aProduct()
                .withId(1L)
                .withNombre("Curso Spring Boot")
                .withDescripcion("Curso backend avanzado")
                .withPrecio(new BigDecimal("120000"))
                .withTipoProducto("Curso")
                .withActivo(true)
                .withUsuarioId(1L)
                .build();

        // Act
        ProductResponse response = mapper.toResponse(product);

        // Assert
        assertNotNull(response);

        assertEquals(
                product.getNombre(),
                response.getNombre()
        );

        assertEquals(
                product.getDescripcion(),
                response.getDescripcion()
        );

        assertEquals(
                product.getPrecio(),
                response.getPrecio()
        );

        assertEquals(
                product.getTipoProducto(),
                response.getTipoProducto()
        );

        assertEquals(
                product.isActivo(),
                response.isActivo()
        );

        assertEquals(
                product.getFechaCreacion(),
                response.getFechaCreacion()
        );
    }
}