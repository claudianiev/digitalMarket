package com.digitalMarket.productservice.application.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterRequest {
    private String nombre;
    private String tipoProducto;
    private Boolean activo;
    private BigDecimal precioMin;
    private BigDecimal precioMax;
    private int page;
    private int size;
}
