package com.digitalMarket.productservice.application.service;

import com.digitalMarket.productservice.application.dto.filter.ProductFilterRequest;
import com.digitalMarket.productservice.domain.exception.ProductNotFoundException;
import com.digitalMarket.productservice.domain.model.Product;
import com.digitalMarket.productservice.domain.port.output.ProductRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepositoryPort repository;

    public ProductService(ProductRepositoryPort repository) {
        this.repository = repository;
    }

    public Product create(String nombre, String descripcion,
                          BigDecimal precio, String tipoProducto,
                          Long usuarioId) {

        Product product = Product.create(
                nombre, descripcion, precio, tipoProducto, usuarioId
        );

        return repository.save(product);
    }

    public List<Product> getActivos() {
        return repository.findByActivoTrue()
                .stream()
                .toList();
    }

    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public void desactivar(Long id) {
        Product product = getById(id);
        product.desactivar();
        repository.save(product);
    }


    public Page<Product> execute(ProductFilterRequest productFilter) {
        return repository.search(productFilter);
    }

}