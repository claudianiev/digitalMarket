package com.digitalMarket.productservice.infrastructure.adapter.persistence.mapper;

import com.digitalMarket.productservice.application.dto.response.ProductResponse;
import com.digitalMarket.productservice.domain.model.Product;
import com.digitalMarket.productservice.infrastructure.adapter.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public ProductMapper() {
    }

    public Product toDomain(ProductEntity e) {
        return Product.restore(
                e.getProductoId(),
                e.getNombre(),
                e.getDescripcion(),
                e.getPrecio(),
                e.getTipoProducto(),
                e.getActivo(),
                e.getFechaCreacion(),
                e.getUsuarioId()
        );
    }

    public static ProductEntity toEntity(Product p) {
        ProductEntity e = new ProductEntity();
        e.setProductoId(p.getId());
        e.setNombre(p.getNombre());
        e.setDescripcion(p.getDescripcion());
        e.setPrecio(p.getPrecio());
        e.setTipoProducto(p.getTipoProducto());
        e.setActivo(p.isActivo());
        e.setFechaCreacion(p.getFechaCreacion());
        e.setUsuarioId(p.getUsuarioId());

        System.out.println(p.getDescripcion());
        System.out.println(e.getDescripcion());
        return e;
    }

    public List<Product> toDomainList(List<ProductEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    public ProductResponse toResponse(Product product) {

        if (product == null) {
            return null;
        }

        ProductResponse response = new ProductResponse();

        response.setNombre(product.getNombre());
        response.setDescripcion(product.getDescripcion());
        response.setPrecio(product.getPrecio());
        response.setTipoProducto(product.getTipoProducto());
        response.setActivo(product.isActivo());
        response.setFechaCreacion(product.getFechaCreacion());

        return response;
    }
}