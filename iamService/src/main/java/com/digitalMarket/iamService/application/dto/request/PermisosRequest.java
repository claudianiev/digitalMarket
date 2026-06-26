package com.digitalMarket.iamService.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PermisosRequest implements Serializable {
    private String nombre;
    private String descripcion;
}
