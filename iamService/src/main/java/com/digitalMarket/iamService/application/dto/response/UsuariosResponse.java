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
public class UsuariosResponse implements Serializable {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Boolean activo;
}