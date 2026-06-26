package com.digitalMarket.productservice.infrastructure.adapter.persistence;

import com.digitalMarket.productservice.application.dto.filter.ProductFilterRequest;
import com.digitalMarket.productservice.domain.model.Product;
import com.digitalMarket.productservice.domain.port.output.ProductRepositoryPort;
import com.digitalMarket.productservice.infrastructure.adapter.persistence.entity.ProductEntity;
import com.digitalMarket.productservice.infrastructure.adapter.persistence.mapper.ProductMapper;
import com.digitalMarket.productservice.infrastructure.adapter.persistence.repository.JpaProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final JpaProductRepository repository;
    private ProductMapper mapper;

    public ProductRepositoryAdapter(JpaProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Product save(Product product) {

        ProductEntity entity = mapper.toEntity(product);

        ProductEntity savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {

        return repository.findById(id)
                .map(mapper::toDomain);
    }

    public List<Product> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> findByActivoTrue() {
        return List.of();
    }


    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);
    }

    @Override
    public Page<Product> search(ProductFilterRequest productFilter) {

        Pageable pageable = PageRequest.of(
                productFilter.getPage(),
                productFilter.getSize()
        );

        Specification<ProductEntity> specification =
                ProductSpecification.build(productFilter);

        return repository.findAll(specification, pageable)
                .map(mapper::toDomain);
    }
}