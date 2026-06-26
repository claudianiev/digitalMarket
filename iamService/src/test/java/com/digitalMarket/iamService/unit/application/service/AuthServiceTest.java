package com.digitalMarket.iamService.unit.application.service;

import com.digitalMarket.iamService.domain.permisos.Permisos;
import com.digitalMarket.iamService.domain.permisos.PermisosRepository;
import com.digitalMarket.iamService.domain.roles.RolRepository;
import com.digitalMarket.iamService.domain.roles.Roles;
import com.digitalMarket.iamService.application.service.AuthService;
import com.digitalMarket.iamService.domain.usuario.Usuarios;
import com.digitalMarket.iamService.domain.usuario.UsuariosRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("testcontainers")
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthServiceTest {

    @Mock
    UsuariosRepository usuariosRepository;
    @Mock
    RolRepository rolRepository;
    @Mock
    PermisosRepository permisosRepository;

    @InjectMocks
    AuthService authService;

    private Usuarios usuario;
    private Roles rol;
    private Permisos permisos;

    @BeforeEach
    void setUp() {
        usuario = new Usuarios();
        usuario.setId(1);
        usuario.setNombre("Claudia");
        usuario.setEmail("claudianiev@gmail.com");
        usuario.setRoles(new HashSet<>());

        rol = new Roles();
        rol.setRolId(1);
        rol.setNombre("ADMINISTRADOR");

        permisos = new Permisos();
        permisos.setId(1);
        permisos.setNombre("GESTIONAR_USUARIOS");
    }

    @Test
    void asignarRolAUsuario_CuandoRolyUsuarioExisten_DebePersistir() {
        //Arranque
        when(usuariosRepository.findByEmail(usuario.getEmail())).thenReturn(usuario);
        when(rolRepository.findByRolId(rol.getRolId())).thenReturn(Optional.of(rol));
        when(usuariosRepository.save(any(Usuarios.class))).thenReturn(usuario);

        //When
        authService.asignarRolAUsuario(usuario.getEmail(), rol.getRolId());

        //Assert
        assertTrue(usuario.getRoles().contains(rol));
        verify(usuariosRepository).save(usuario);
    }

    @Test
    void asignarRolAUsuario_CuandoRolyUsuarioNoExisten_DebeFallar() {
        // Arrange
        when(usuariosRepository.findByEmail(usuario.getEmail())).thenReturn(usuario);
        when(rolRepository.findByRolId(rol.getRolId())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.asignarRolAUsuario(usuario.getEmail(), rol.getRolId())
        );

        // Assert
        assertTrue(exception.getMessage().contains("Rol no encontrado"));
    }

    @Test
    void asignarPermisoARol_CuandoRolNoExiste_DebeFallar() {
        // Arrange
        when(rolRepository.findByRolId(rol.getRolId())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.asignarPermisoARol(rol.getRolId(), permisos.getId())
        );

        assertTrue(exception.getMessage().contains("Rol no encontrado"));
    }

    @Test
    void asignarPermisoARol_CuandoPermisoNoExiste_NoDebeAgregarNada() {
        // Arrange
        when(rolRepository.findByRolId(rol.getRolId())).thenReturn(Optional.of(rol));
        when(permisosRepository.findById(2)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                authService.asignarPermisoARol(rol.getRolId(), 2)
        );

        Assertions.assertTrue(exception.getMessage().contains("Permiso no encontrado"));
    }
}