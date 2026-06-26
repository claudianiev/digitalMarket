package com.digitalMarket.iamService.unit.application.service;

import com.digitalMarket.iamService.application.dto.request.UsuariosRequest;
import com.digitalMarket.iamService.application.dto.response.UsuariosResponse;
import com.digitalMarket.iamService.application.service.UsuariosService;
import com.digitalMarket.iamService.domain.usuario.Usuarios;
import com.digitalMarket.iamService.domain.usuario.UsuariosRepository;
import com.digitalMarket.iamService.infraestructure.exceptions.IdNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;


import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("testcontainers")
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuariosServiceTest {

    @Mock
    private UsuariosRepository usuariosRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuariosService usuariosService;
    private UsuariosRequest usuarioRequest;

    @BeforeEach
    void setUp() {
        System.out.println(">> Ejecutando setUp()");
        initUniarioRequest();
    }

     void initUniarioRequest() {
        usuarioRequest = new UsuariosRequest();
        usuarioRequest.setNombre("Claudia");
        usuarioRequest.setApellido("Nieves");
        usuarioRequest.setEmail("claudia.nieves@dominio.com");
        usuarioRequest.setPasswordHash("123456");
        usuarioRequest.setActivo(true);
    }
    // ---------- CREACIÓN ----------
    @Test
    public void crearUsuario_CuandoDatosValidos_DebePersistir() {
        // Arrange
        Usuarios usuarioMock = new Usuarios();
        usuarioMock.setId(1);
        usuarioMock.setNombre(usuarioRequest.getNombre());
        usuarioMock.setApellido(usuarioRequest.getApellido());
        usuarioMock.setEmail(usuarioRequest.getEmail());
        usuarioMock.setPasswordHash(usuarioRequest.getPasswordHash());
        usuarioMock.setActivo(true);
        usuarioMock.setFechaCreacion(Instant.now());

        when(passwordEncoder.encode(usuarioRequest.getPasswordHash()))
                .thenReturn("hashed_password");

        when(usuariosRepository.save(any(Usuarios.class)))
                .thenReturn(usuarioMock);

        // Act
        UsuariosResponse response = usuariosService.create(usuarioRequest);

        // Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getEmail()).isEqualTo(usuarioRequest.getEmail());
        verify(usuariosRepository, times(1)).save(any(Usuarios.class));
        verify(passwordEncoder, times(1)).encode(usuarioRequest.getPasswordHash());
    }

    @Test
    public void crearUsuario_CuandoEmailDuplicado_DebeFallar() {
        // Arrange
        Usuarios usuarioExistente = new Usuarios();
        usuarioExistente.setId(1);
        usuarioExistente.setEmail("claudia.nieves@dominio.com");

        when(usuariosRepository.findByEmail(usuarioRequest.getEmail()))
                .thenReturn(usuarioExistente);

        // Act & Assert
        Assertions.assertThatThrownBy(() -> usuariosService.create(usuarioRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El correo ya está registrado");

        verify(usuariosRepository, never()).save(any());
    }



    // ---------- CONSULTA ----------
    @Test
    public void obtenerUsuarioPorId_CuandoExiste_DebeRetornarUsuario() {
        // Arrange
        Usuarios usuarioMock = new Usuarios();
        usuarioMock.setId(1);
        usuarioMock.setNombre(usuarioRequest.getNombre());
        usuarioMock.setApellido(usuarioRequest.getApellido());
        usuarioMock.setEmail(usuarioRequest.getEmail());
        usuarioMock.setPasswordHash(usuarioRequest.getPasswordHash());
        usuarioMock.setActivo(true);
        usuarioMock.setFechaCreacion(Instant.now());

        when(usuariosRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        // Act
        UsuariosResponse response = usuariosService.read(1L);

        // Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getEmail()).isEqualTo("claudia.nieves@dominio.com");
        Assertions.assertThat(response.getNombre()).isEqualTo("Claudia");
        Assertions.assertThat(response.getActivo().compareTo(true));

        verify(usuariosRepository,times(1)).findById(1L);
    }

    @Test
    public void obtenerUsuarioPorId_CuandoNoExiste_DebeRetornarVacio() {
        // Arrange
        Long id = 99L;
        when(usuariosRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> usuariosService.read(id))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessageContaining("Registro no se encuentra en usuarios");
    }

    // ---------- ELIMINACIÓN ----------
    @Test
    public void eliminarUsuario_CuandoExiste_DebeEliminar() {
        // Arrange
        int usuarioId = 1;
        Usuarios usuarioExistente = new Usuarios();
        usuarioExistente.setId(usuarioId);

        when(usuariosRepository.findById((long) usuarioId)).thenReturn(Optional.of(usuarioExistente));

        // Act
        assertDoesNotThrow(() -> usuariosService.delete((long) usuarioId));

        // Assert
        verify(usuariosRepository, times(1)).delete(usuarioExistente);
    }

    @Test
    public void eliminarUsuario_CuandoNoExiste_DebeFallar() {
        // Arrange
        Long usuarioId = 1L;

        when(usuariosRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuariosService.delete(usuarioId);
        });

        assertEquals("Registro no se encuentra en usuarios", exception.getMessage());

        // Verificar que delete nunca se llamó
        verify(usuariosRepository, never()).delete(any());
    }

}