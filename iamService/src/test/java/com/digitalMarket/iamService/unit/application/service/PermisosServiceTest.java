package com.digitalMarket.iamService.unit.application.service;

import com.digitalMarket.iamService.application.dto.request.PermisosRequest;
import com.digitalMarket.iamService.application.dto.response.PermisosResponse;
import com.digitalMarket.iamService.application.service.PermisosService;
import com.digitalMarket.iamService.domain.permisos.Permisos;
import com.digitalMarket.iamService.domain.permisos.PermisosRepository;
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

import static java.util.Optional.empty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("testcontainers")
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PermisosServiceTest {
    @Mock
    PermisosRepository permisosRepository;

    @InjectMocks
    PermisosService permisosService;

    PermisosRequest permisosRequest;

    @BeforeEach
    void setUp() {
        initRolRequest();
    }

    void initRolRequest() {
        permisosRequest = PermisosRequest.builder()
            .nombre("Asignar Roles")
            .descripcion("Gestiona asignacion de roles")
            .build();
    }

    @Test
    void crearPermiso_cuandoDatosValidos_DebePersistir(){
        when(permisosRepository.findByNombre(anyString())).thenReturn(empty());

        when(permisosRepository.save(any(Permisos.class)))
                .thenAnswer(invocation -> {
                    Permisos permisos = invocation.getArgument(0);
                    permisos.setId(1);
                    return permisos;
                });

        // Act
        PermisosResponse response = permisosService.create(permisosRequest);

        // Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getNombre()).isEqualTo(permisosRequest.getNombre());

        verify(permisosRepository, times(1)).findByNombre(permisosRequest.getNombre());
        verify(permisosRepository, times(1)).save(any(Permisos.class));
    }

    @Test
    void  crearPermiso_cuandoYaExiste_DebeFallar(){
        // Arrange
        Permisos permisoExistente = Permisos.builder()
                .id(1)
                .nombre(permisosRequest.getNombre())
                .descripcion(permisosRequest.getDescripcion())
                .build();

        when(permisosRepository.findByNombre(permisosRequest.getNombre()))
                .thenReturn(Optional.of(permisoExistente));

        // Act + Assert
        Assertions.assertThatThrownBy(() -> permisosService.create(permisosRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El permiso ya está registrado");

        verify(permisosRepository, times(1)).findByNombre(permisosRequest.getNombre());
        verify(permisosRepository, never()).save(any(Permisos.class));
    }

    @Test
    void crearPermiso_cuandoRequestEsNulo_DebeFallar(){
        Assertions.assertThatThrownBy(() -> permisosService.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La solicitud no puede ser nula");
        verify(permisosRepository, never()).save(any());
    }


    @Test
    void leerPermiso_cuandoExiste_DebeRetornarResponse() {
        Permisos permiso = Permisos.builder()
                .id(1)
                .nombre("LEER_USUARIO")
                .descripcion("Permite ver los usuarios")
                .build();

        when(permisosRepository.findById(1L)).thenReturn(Optional.of(permiso));

        PermisosResponse response = permisosService.read(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getNombre()).isEqualTo("LEER_USUARIO");
    }

    @Test
    void leerPermiso_cuandoNoExiste_DebeLanzarExcepcion() {
        when(permisosRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> permisosService.read(99L))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessageContaining("Registro no se encuentra en permisos");
    }


    @Test
    void actualizarPermiso_cuandoExiste_DebeActualizarDatos() {
        Permisos permisoExistente = Permisos.builder()
                .id(1)
                .nombre("LEER_USUARIO")
                .descripcion("Antigua")
                .build();

        when(permisosRepository.findById(1L)).thenReturn(Optional.of(permisoExistente));
        when(permisosRepository.save(any(Permisos.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PermisosResponse response = permisosService.update(permisosRequest,1L);

        Assertions.assertThat(response.getDescripcion()).isEqualTo(permisosRequest.getDescripcion());
        verify(permisosRepository).save(any(Permisos.class));
    }

    @Test
    void actualizarPermiso_cuandoNoExiste_DebeLanzarExcepcion() {
        when(permisosRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> permisosService.update(permisosRequest,99L))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessageContaining("Registro no se encuentra en permisos");
    }

    @Test
    void actualizarPermiso_cuandoRequestEsNulo_DebeLanzarExcepcion() {
        Assertions.assertThatThrownBy(() -> permisosService.update(null, 99L))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessageContaining("Registro no se encuentra en permisos");
    }


    @Test
    void eliminarPermiso_cuandoExiste_DebeEliminar() {
        Permisos permiso = Permisos.builder().id(1).nombre("BORRAR_USUARIO").build();
        when(permisosRepository.findById(1L)).thenReturn(Optional.of(permiso));

        permisosService.delete(1L);

        verify(permisosRepository, times(1)).delete(permiso);
    }

    @Test
    void eliminarPermiso_cuandoNoExiste_DebeLanzarExcepcion() {
        when(permisosRepository.findById(1L)).thenReturn(empty());

        Assertions.assertThatThrownBy(() -> permisosService.delete(1L))
                .isInstanceOf(IdNotFoundException.class)
                .hasMessageContaining("Registro no se encuentra en permisos");

        verify(permisosRepository, never()).delete(any());
    }
}