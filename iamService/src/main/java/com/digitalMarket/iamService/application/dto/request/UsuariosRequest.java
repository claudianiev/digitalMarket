package com.digitalMarket.iamService.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UsuariosRequest implements Serializable {

    @NotBlank(message = "usuario es obligatorio.")
    @NotNull(message = "usuario es obligatorio.")
    private String nombre;

    @NotBlank(message = "apellido es obligatorio.")
    @NotNull(message = "apellido es obligatorio.")
    private String apellido;

    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Formato de email inválido"
    )
    @NotNull(message = "El email es obligatorio")
    @Email
    private String email;

    @NotNull(message = "password es obligatorio")
    @NotBlank(message = "password es obligatorio")
    private String passwordHash;

    private Boolean activo;
}