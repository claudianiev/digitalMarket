package com.digitalMarket.iamService.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UsuariosLoadResponse implements Serializable {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Boolean activo;
    private Set<String> roles;
}
