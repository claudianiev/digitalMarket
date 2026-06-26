package com.digitalMarket.productservice.infrastructure.adapter.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name="productos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class ProductEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productoId;

    private String nombre;
    private String descripcion;
    private BigDecimal precio;

    @Column(name = "tipo_producto")
    private String tipoProducto;

    private Boolean activo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "usuario_id")
    private Long usuarioId;


}