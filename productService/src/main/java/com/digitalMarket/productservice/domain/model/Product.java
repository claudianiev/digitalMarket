package com.digitalMarket.productservice.domain.model;

import com.digitalMarket.productservice.domain.exception.DomainValidationException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class Product {

    private final Long id;
    private final String nombre;
    private final String descripcion;
    private final BigDecimal precio;
    private final String tipoProducto;
    private boolean activo;
    private final LocalDateTime fechaCreacion;
    private final Long usuarioId;

    public Product(Long id, String nombre, String descripcion,
                   BigDecimal precio, String tipoProducto,
                   boolean activo, LocalDateTime fechaCreacion,
                   Long usuarioId) {

        validate(nombre, precio, tipoProducto, usuarioId);

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipoProducto = tipoProducto;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.usuarioId = usuarioId;
    }

    public static Product create(String nombre, String descripcion,
                                 BigDecimal precio, String tipoProducto,
                                 Long usuarioId) {

        return new Product(
                null,
                nombre,
                descripcion,
                precio,
                tipoProducto,
                true,
                LocalDateTime.now(),
                usuarioId
        );
    }

    public static Product restore(Long id, String nombre, String descripcion,
                                  BigDecimal precio, String tipoProducto,
                                  boolean activo, LocalDateTime fechaCreacion,
                                  Long usuarioId) {

        return new Product(id, nombre, descripcion, precio,
                tipoProducto, activo, fechaCreacion, usuarioId);
    }

    public void desactivar() {
        this.activo = false;
    }

    private void validate(String nombre, BigDecimal precio,
                          String tipoProducto, Long usuarioId) {

        if (nombre == null || nombre.isBlank()) {
            throw new DomainValidationException("Nombre requerido");
        }

        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainValidationException("Un producto no puede tener precio negativo");
        }

        if (tipoProducto == null || tipoProducto.isBlank()) {
            throw new DomainValidationException("Tipo requerido");
        }

        if (usuarioId == null) {
            throw new DomainValidationException("Usuario requerido");
        }
    }
}