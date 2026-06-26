package com.digitalMarket.iamService.unit.presentacion;

import com.digitalMarket.iamService.application.dto.request.UsuariosRequest;
import com.digitalMarket.iamService.application.dto.response.UsuariosResponse;
import com.digitalMarket.iamService.application.service.UsuariosService;
import com.digitalMarket.iamService.infraestructure.exceptions.IdNotFoundException;
import com.digitalMarket.iamService.presentation.controllers.UsuariosController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("testcontainers")
@WebMvcTest(controllers = UsuariosController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsuariosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuariosService usuariosService;

    @Value("${app.base-path}")
    private String basePath;

    private UsuariosRequest usuarioRequest;
    @Autowired
    private ObjectMapper objectMapper;


    // ------------------------------
    // CREAR USUARIO
    // ------------------------------
    @Test
    public void crearUsuario_CuandoDatosValidos_DebeRetornar201() throws Exception {

        var response = new UsuariosResponse();
        response.setId(1L);
        response.setNombre("Claudia");
        response.setApellido("Nieves");
        response.setEmail("claudia.nieves@gmail.com");

        when(usuariosService.create(any(UsuariosRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post(basePath +"/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                         "nombre":"Claudia",
                         "apellido":"Nieves",
                         "email":"claudianiev@gmail.com",
                         "passwordHash":"123",
                         "activo": true
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Claudia"))
                .andExpect(jsonPath("$.apellido").value("Nieves"))
                .andExpect(jsonPath("$.email").value("claudia.nieves@gmail.com"));

        // Verify
        verify(usuariosService).create(any());
    }

    @Test
    public void crearUsuario_CuandoEmailDuplicado_DebeRetornar400() throws Exception {
        // Simulamos que el servicio lanza excepción por duplicado
        when(usuariosService.create(any(UsuariosRequest.class)))
                .thenThrow(new IllegalArgumentException("El correo ya está registrado"));

        // Act & Assert
        mockMvc.perform(post(basePath + "/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                         "nombre":"Claudia",
                         "apellido":"Nieves",
                         "email":"claudianiev@gmail.com",
                         "passwordHash":"123",
                         "activo": true
                    }
                """))
                .andExpect(status().isBadRequest()) // 400 esperado
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("El correo ya está registrado"));
    }

    @Test
     public void crearUsuario_CuandoValidacionFalla_DebeRetornar400ConListaDeErrores() throws Exception {
        // Given: body con campos faltantes o inválidos
        String requestBody = """
            {
              "usuario": "",
              "password": "",
              "email": "correo-invalido",
              "apellido": ""
            }
            """;

        // When & Then
        mockMvc.perform(
                post(basePath + "/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").isArray())
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.hasItems(
                        "usuario es obligatorio.",
                        "password es obligatorio",
                        "Formato de email inválido",
                        "apellido es obligatorio."
                )));
    }

    // ------------------------------
    // OBTENER USUARIO POR ID
    // ------------------------------
    @Test
    public void obtenerUsuarioPorId_CuandoExiste_DebeRetornar200() throws Exception {
        // Arrange
        Long id = 1L;
        var response = new UsuariosResponse();
        response.setId(1L);
        response.setNombre("Claudia");
        response.setApellido("Nieves");
        response.setEmail("claudia.nieves@gmail.com");

        // When
        when(usuariosService.read(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(
                get(basePath + "/usuarios/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void obtenerUsuarioPorId_CuandoNoExiste_DebeRetornar404() throws Exception {
        // Arrange
        when(usuariosService.read(99L))
                .thenThrow(new IdNotFoundException("usuarios"));

        // Act & Assert
        mockMvc.perform(get(basePath+"/usuarios/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message[0]")
                .value("Registro no se encuentra en usuarios"));
    }

    // ------------------------------
    // LISTAR USUARIOS
    // ------------------------------
    @Test
    public void listarUsuarios_CuandoExistenUsuarios_DebeRetornar200() throws Exception {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void listarUsuarios_CuandoNoHayUsuarios_DebeRetornarListaVacia() throws Exception {
        // Arrange
        // Act
        // Assert
    }

    // ------------------------------
    // ACTUALIZAR USUARIO
    // ------------------------------
    @Test
    public void actualizarUsuario_CuandoExiste_DebeRetornar200() throws Exception {
        // Arrange
        UsuariosRequest request = new UsuariosRequest();
        request.setNombre("Juan");
        request.setApellido("Pérez");
        request.setEmail("juan@test.com");
        request.setPasswordHash("12345");

        UsuariosResponse response = new UsuariosResponse();
        response.setId(1L);
        response.setNombre("Juan");
        response.setApellido("Pérez");

        when(usuariosService.update(eq(request), eq(1L))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put(basePath+"/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message[0]")
                .value("Registro no se encuentra en usuarios"));
    }

    @Test
    public void actualizarUsuario_CuandoNoExiste_DebeRetornar404() throws Exception {
        // Arrange
        UsuariosRequest request = new UsuariosRequest();
        request.setNombre("Juan");
        request.setApellido("Pérez");
        request.setEmail("juan@test.com");
        request.setPasswordHash("12345");

        when(usuariosService.update(eq(request), eq(99L)))
                .thenThrow(new IdNotFoundException("usuarios"));

        // Act & Assert
        mockMvc.perform(put(basePath+"/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message[0]")
                .value("Registro no se encuentra en usuarios"));
    }

    // ------------------------------
    // ELIMINAR USUARIO
    // ------------------------------
    @Test
    public void eliminarUsuario_CuandoExiste_DebeRetornar204() throws Exception {
        doNothing().when(usuariosService).delete(1L);
        mockMvc.perform(delete(basePath+"/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void eliminarUsuario_CuandoNoExiste_DebeRetornar404() throws Exception {
        doThrow(new IdNotFoundException("usuarios"))
                .when(usuariosService).delete(999L);

        mockMvc.perform(delete(basePath+"/usuarios/999"))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message[0]")
                .value("Registro no se encuentra en usuarios"));
    }

    // ------------------------------
    // VALIDACIONES DE SEGURIDAD (opcional)
    // ------------------------------
    @Test
    public void crearUsuario_CuandoNoAutenticado_DebeRetornar401() throws Exception {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void eliminarUsuario_CuandoSinRolAdmin_DebeRetornar403() throws Exception {
        // Arrange
        // Act
        // Assert
    }
}