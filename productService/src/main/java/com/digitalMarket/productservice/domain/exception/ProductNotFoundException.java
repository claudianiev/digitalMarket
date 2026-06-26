package com.digitalMarket.productservice.domain.exception;

public class ProductNotFoundException extends DomainValidationException  {
  public ProductNotFoundException(Long id) {
    super("Producto con id " + id + " no encontrado");
  }
}
