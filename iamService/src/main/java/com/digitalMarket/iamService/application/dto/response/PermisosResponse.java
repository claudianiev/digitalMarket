package com.digitalMarket.iamService.application.dto.response;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PermisosResponse implements Serializable {
    @Size(max = 50, message = "El tamaño máximo es de 50 caracteres.")
    @NotNull(message = "No puede ir campo null")
    @NotBlank(message = "Nombre del rol requerido")
    private String nombre;
    private String descripcion;
}