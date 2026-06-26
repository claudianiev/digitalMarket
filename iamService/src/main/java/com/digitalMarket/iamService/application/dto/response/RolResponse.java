package com.digitalMarket.iamService.application.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RolResponse implements Serializable {
    private String nombre;
    private String descripcion;
   // private Set<PermisosEntity> permisos;
}