package com.digitalMarket.iamService.unit.application.service;

import com.digitalMarket.iamService.application.dto.request.RolRequest;
import com.digitalMarket.iamService.application.dto.response.RolResponse;
import com.digitalMarket.iamService.application.service.RolService;
import com.digitalMarket.iamService.domain.roles.RolRepository;
import com.digitalMarket.iamService.domain.roles.Roles;
import com.digitalMarket.iamService.infraestructure.exceptions.IdNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ActiveProfiles("testcontainers")
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RolServiceTest {

    @Mock
    RolRepository rolRepository;

    @InjectMocks
    RolService rolService;


    RolRequest rolRequest;

    @BeforeEach
    void setUp() {
        initRolRequest();
    }

    void initRolRequest() {
        rolRequest = RolRequest.builder()
                .nombre("ADMINISTRADOR")
                .descripcion("Gestiona asignacion de roles y permisos")
                .build();
    }

    @Test
    void crearRol_cuandoDatosValidos_DebePersistir() {
        when(rolRepository.findByNombre(anyString()))
                .thenReturn(Optional.empty());

        when(rolRepository.save(any(Roles.class)))
                .thenAnswer(invocation -> {
                    Roles rol = invocation.getArgument(0);
                    rol.setRolId(1);
                    return rol;
                });
        // Act
        RolResponse response = rolService.create(rolRequest);

        // Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getNombre()).isEqualTo(rolRequest.getNombre());

        verify(rolRepository, times(1)).findByNombre(rolRequest.getNombre());
        verify(rolRepository, times(1)).save(any(Roles.class));
    }

    @Test
    void crearRol_cuandoYaExiste_DebeFallar() {
        // Arrange
        Roles rolExistente = Roles.builder()
                .rolId(1)
                .nombre(rolRequest.getNombre())
                .descripcion(rolRequest.getDescripcion())
                .build();

        when(rolRepository.findByNombre(rolRequest.getNombre()))
                .thenReturn(Optional.of(rolExistente));

        // Act + Assert
        Assertions.assertThatThrownBy(() -> rolService.create(rolRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El rol ya está registrado");

        verify(rolRepository, times(1)).findByNombre(rolRequest.getNombre());
        verify(rolRepository, never()).save(any(Roles.class));
    }


    @Test
    void crearRol_cuandoRequestEsNulo_DebeFallar() {
        Assertions.assertThatThrownBy(() -> rolService.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La solicitud no puede ser nula");
        verify(rolRepository, never()).save(any());
    }

    @Test
    void leerRol_cuandoExiste_DebeRetornarResponse() {
        Roles rol = Roles.builder()
                .rolId(1)
                .nombre("ADMIN")
                .descripcion("Rol administrador")
                .build();

        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));

        RolResponse response = rolService.read(1);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getNombre()).isEqualTo("ADMIN");
        verify(rolRepository, times(1)).findById(1);
    }

    @Test
    void leerRol_cuandoNoExiste_DebeLanzarExcepcion() {
        when(rolRepository.findById(99)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> rolService.read(99))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessageContaining("Registro no se encuentra en rol");

        verify(rolRepository, times(1)).findById(99);
    }

    @Test
    void actualizarRol_cuandoExiste_DebeActualizarDatos() {
        Roles rolExistente = Roles.builder()
                .rolId(1)
                .nombre("ADMIN")
                .descripcion("Desc vieja")
                .build();

        when(rolRepository.findById(1)).thenReturn(Optional.of(rolExistente));
        when(rolRepository.save(any(Roles.class))).thenAnswer(inv -> inv.getArgument(0));

        RolRequest updateRequest = RolRequest.builder()
                .nombre("ADMINISTRADOR")
                .descripcion("Gestiona roles")
                .build();

        RolResponse response = rolService.update(updateRequest,1);

        Assertions.assertThat(response.getDescripcion()).isEqualTo("Gestiona roles");
        verify(rolRepository).save(any(Roles.class));
    }

    @Test
    void actualizarRol_cuandoNoExiste_DebeLanzarExcepcion() {
        when(rolRepository.findById(999)).thenReturn(Optional.empty());

        RolRequest updateRequest = RolRequest.builder()
                .nombre("X")
                .descripcion("Y")
                .build();

        Assertions.assertThatThrownBy(() -> rolService.update(updateRequest,999))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessageContaining("Registro no se encuentra en rol");

        verify(rolRepository, never()).save(any());
    }

    @Test
    void eliminarRol_cuandoExiste_DebeEliminar() {
        Roles rol = Roles.builder().rolId(1).nombre("ADMIN").build();
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));

        rolService.delete(1);

        verify(rolRepository).delete(rol);
    }

    @Test
    void eliminarRol_cuandoNoExiste_DebeLanzarExcepcion() {
        when(rolRepository.findById(99)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> rolService.delete(99))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessageContaining("Registro no se encuentra en rol");

        verify(rolRepository, never()).delete(any());
    }
}