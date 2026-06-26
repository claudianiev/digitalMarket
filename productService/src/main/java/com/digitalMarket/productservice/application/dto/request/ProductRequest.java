package com.digitalMarket.productservice.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class ProductRequest {
    public String nombre;
    public String descripcion;
    public BigDecimal precio;
    public String tipoProducto;
    public Long usuarioId;

    public ProductRequest() {

    }
}