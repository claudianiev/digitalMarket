package com.digitalMarket.productservice.interfaces;

import com.digitalMarket.productservice.application.dto.request.ProductRequest;
import com.digitalMarket.productservice.application.dto.response.ProductResponse;
import com.digitalMarket.productservice.application.service.ProductService;
import com.digitalMarket.productservice.domain.exception.ProductNotFoundException;
import com.digitalMarket.productservice.domain.model.Product;
import com.digitalMarket.productservice.infrastructure.adapter.persistence.mapper.ProductMapper;
import com.digitalMarket.productservice.infrastructure.rest.ProductController;
import com.digitalMarket.productservice.infrastructure.rest.error.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class)
public class ProductControllerTest {
    @MockitoBean
    private ProductService service;

    @MockitoBean
    private ProductMapper mapper;


    private ProductController controller;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    @DisplayName("Debe crear correctamente un producto mediante POST /products")
    void shouldCreateProductEndpoint() throws Exception {

        // Arrange
        ProductRequest request = new ProductRequest();

        request.setNombre("Curso Spring Boot");
        request.setDescripcion("Curso backend avanzado");
        request.setPrecio(new BigDecimal("120000"));
        request.setTipoProducto("Curso");
        request.setUsuarioId(1L);

        Product product = Product.restore(
                1L,
                "Curso Spring Boot",
                "Curso backend avanzado",
                new BigDecimal("120000"),
                "Curso",
                true,
                LocalDateTime.now(),
                1L
        );

        ProductResponse response = new ProductResponse();

        response.setId(1L);
        response.setNombre("Curso Spring Boot");
        response.setDescripcion("Curso backend avanzado");
        response.setPrecio(new BigDecimal("120000"));
        response.setTipoProducto("Curso");
        response.setActivo(true);
        response.setFechaCreacion(LocalDateTime.now());

        when(service.create(
                anyString(),
                anyString(),
                any(BigDecimal.class),
                anyString(),
                anyLong()
        )).thenReturn(product);

        when(mapper.toResponse(product))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(
                        post("/productos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre")
                        .value("Curso Spring Boot"))
                .andExpect(jsonPath("$.descripcion")
                        .value("Curso backend avanzado"))
                .andExpect(jsonPath("$.precio")
                        .value(120000))
                .andExpect(jsonPath("$.tipoProducto")
                        .value("Curso"));

        // Verify Behavior
        verify(service).create(
                request.getNombre(),
                request.getDescripcion(),
                request.getPrecio(),
                request.getTipoProducto(),
                request.getUsuarioId()
        );

        verify(mapper).toResponse(product);
    }


    @Test
    @DisplayName("Debe retornar correctamente un producto por id mediante GET /productos/{id}")
    void shouldReturnProductByIdEndpoint() throws Exception {

        // Arrange
        Long productId = 1L;

        Product product = Product.restore(
                1L,
                "Curso AWS",
                "AWS Developer Associate",
                new BigDecimal("50000"),
                "Video",
                true,
                LocalDateTime.now(),
                1L
        );

        ProductResponse response = new ProductResponse();

        response.setId(1L);
        response.setNombre("Curso AWS");
        response.setDescripcion("AWS Developer Associate");
        response.setPrecio(new BigDecimal("50000"));
        response.setTipoProducto("Video");
        response.setFechaCreacion(LocalDateTime.now());

        when(service.getById(productId))
                .thenReturn(product);

        when(mapper.toResponse(product))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(
                        get("/productos/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(1))
                .andExpect(jsonPath("$.nombre")
                        .value("Curso AWS"))
                .andExpect(jsonPath("$.descripcion")
                        .value("AWS Developer Associate"))
                .andExpect(jsonPath("$.precio")
                        .value(50000))
                .andExpect(jsonPath("$.tipoProducto")
                        .value("Video")
                );

        // Verify Behavior Pattern
        verify(service).getById(productId);

        verify(mapper).toResponse(product);
    }

    @Test
    @DisplayName("Debe retornar 404 cuando el producto no existe")
    void shouldReturn404WhenProductNotFound() throws Exception {

        // Arrange
        Long productId = 99L;

        when(service.getById(productId))
                .thenThrow(
                        new ProductNotFoundException(productId)
                );

        // Act + Assert
        mockMvc.perform(
                        get("/productos/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.message")
                        .value("Producto con id " + productId + " no encontrado"))

                .andExpect(jsonPath("$.status")
                        .value(404))

                .andExpect(jsonPath("$.timestamp")
                        .exists());

        // Verify Behavior Pattern
        verify(service).getById(productId);

        verifyNoInteractions(mapper);
    }
}