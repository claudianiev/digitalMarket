package com.digitalMarket.productservice.unit.application;

import com.digitalMarket.productservice.domain.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductBuilder {
    private Long id = 1L;
    private String nombre = "Producto Test";
    private String descripcion = "Descripcion Test";
    private BigDecimal precio = new BigDecimal("10000");
    private String tipoProducto = "Ebook";
    private Boolean activo = true;
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private Long usuarioId = 1L;

    private ProductBuilder() {
    }

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public ProductBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProductBuilder withNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ProductBuilder withDescripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public ProductBuilder withPrecio(BigDecimal precio) {
        this.precio = precio;
        return this;
    }

    public ProductBuilder withTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
        return this;
    }

    public ProductBuilder withActivo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public ProductBuilder withUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
        return this;
    }

    public Product build() {

        return new Product(
                id,
                nombre,
                descripcion,
                precio,
                tipoProducto,
                activo,
                fechaCreacion,
                usuarioId
        );
    }
}
