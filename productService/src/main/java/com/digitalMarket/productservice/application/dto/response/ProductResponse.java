package com.digitalMarket.productservice.application.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class ProductResponse {
    private  Long id;
    private  String nombre;
    private  String descripcion;
    private  BigDecimal precio;
    private  String tipoProducto;
    private  boolean activo;
    private  LocalDateTime fechaCreacion;
}