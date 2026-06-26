package com.digitalMarket.productservice.integration.infrastructure;

import com.digitalMarket.productservice.domain.model.Product;
import com.digitalMarket.productservice.infrastructure.adapter.persistence.entity.ProductEntity;
import com.digitalMarket.productservice.infrastructure.adapter.persistence.mapper.ProductMapper;
import com.digitalMarket.productservice.infrastructure.adapter.persistence.repository.JpaProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("testcontainers")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.ANY
)
public class ProductInfrastructure {
    @Autowired
    private JpaProductRepository repository;
    private final ProductMapper mapper =
            new ProductMapper();

    @Test
    @DisplayName("Debe persistir correctamente ProductEntity en base de datos")
    public void shouldPersistProductEntity() {

        // Arrange - Test Data Factory Pattern
        Product product =
                ProductTestDataFactory.createValidProduct();

        ProductEntity entity =
                ProductMapper.toEntity(product);

        // Act
        ProductEntity savedEntity =
                repository.save(entity);

        // Assert - Persistencia real
        assertNotNull(savedEntity);

        assertNotNull(savedEntity.getProductoId());

        Optional<ProductEntity> result =
                repository.findById(savedEntity.getProductoId());

        assertTrue(result.isPresent());

        ProductEntity persisted = result.get();

        assertEquals(
                product.getNombre(),
                persisted.getNombre()
        );

        assertEquals(
                product.getDescripcion(),
                persisted.getDescripcion()
        );

        assertEquals(
                product.getPrecio(),
                persisted.getPrecio()
        );

        assertEquals(
                product.getTipoProducto(),
                persisted.getTipoProducto()
        );

        assertEquals(
                product.isActivo(),
                persisted.getActivo()
        );

        assertEquals(
                product.getUsuarioId(),
                persisted.getUsuarioId()
        );
    }

    @Test
    @DisplayName("Debe mapear correctamente ProductEntity hacia Product")
    void shouldMapEntityToDomain() {

        // Arrange
        LocalDateTime fechaCreacion = LocalDateTime.now();

        ProductEntity entity = new ProductEntity();

        entity.setProductoId(1L);
        entity.setNombre("Curso Spring Boot");
        entity.setDescripcion("Curso backend avanzado");
        entity.setPrecio(new BigDecimal("120000"));
        entity.setTipoProducto("Curso");
        entity.setActivo(true);
        entity.setFechaCreacion(fechaCreacion);
        entity.setUsuarioId(1L);

        // Act
        Product product = mapper.toDomain(entity);

        // Assert
        assertNotNull(product);

        assertEquals(
                entity.getProductoId(),
                product.getId()
        );

        assertEquals(
                entity.getNombre(),
                product.getNombre()
        );

        assertEquals(
                entity.getDescripcion(),
                product.getDescripcion()
        );

        assertEquals(
                entity.getPrecio(),
                product.getPrecio()
        );

        assertEquals(
                entity.getTipoProducto(),
                product.getTipoProducto()
        );

        assertEquals(
                entity.getActivo(),
                product.isActivo()
        );

        assertEquals(
                entity.getFechaCreacion(),
                product.getFechaCreacion()
        );

        assertEquals(
                entity.getUsuarioId(),
                product.getUsuarioId()
        );
    }
}
