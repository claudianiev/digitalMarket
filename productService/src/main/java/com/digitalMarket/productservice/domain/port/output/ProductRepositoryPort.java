package com.digitalMarket.productservice.domain.port.output;

import com.digitalMarket.productservice.application.dto.filter.ProductFilterRequest;
import com.digitalMarket.productservice.domain.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    List<Product> findByActivoTrue();

    Page<Product> search(ProductFilterRequest productFilter);

    void deleteById(Long id);
}