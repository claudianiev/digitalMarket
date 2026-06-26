package com.digitalMarket.iamService.unit.application.request;

import com.digitalMarket.iamService.application.dto.request.UsuariosRequest;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Set;


@ActiveProfiles("testcontainers")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuariosRequestTest {

    private Validator validator;
    private UsuariosRequest usuarioRequest;

    @BeforeEach
    void setUp() {
        validator = createValidator();
        Assertions.assertThat(validator).isNotNull();
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

    private Validator createValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    @ParameterizedTest
    @CsvSource(value={
            "Claudia,true,''", //Caso válido
            ",false,usuario es obligatorio.", //Caso null
            "'',false,usuario es obligatorio." // Caso vacio
    },nullValues = "null")
    void validateNombreCase(String nombre,boolean expectedValid, String message) {
        //Arranque
        usuarioRequest.setNombre(nombre);
        ActualAssetions(expectedValid,message);
    }


    @ParameterizedTest
    @CsvSource(value={
            "Nieves,true,''", //Caso válido
            ",false,apellido es obligatorio.", //Caso null
            "'',false,apellido es obligatorio." // Caso vacio
    },nullValues = "null")
    void validateApellidoCase(String apellido,boolean expectedValid, String message) {
        //Arranque
        usuarioRequest.setApellido(apellido);
        ActualAssetions(expectedValid,message);
    }


    @ParameterizedTest
    @CsvSource(value={
            "pepito.perez@dominio.com,true,''", //Caso válido,
            ",false,El email es obligatorio", //Caso null
            ",false,El email es obligatorio", //Caso vacío
            "pepito.perez@@dominio.com,false,Formato de email inválido", //Caso con formato inválido
            "pepito.perez@,false,Formato de email inválido", // Caso sin dominio
            "@dominio.com,false,Formato de email inválido", //Caso sin usuario
            "pepito.perez@gmail,false,Formato de email inválido",// Caso sin extensión
            "pepito.perez#gmail.com,false,Formato de email inválido", //Caso con caracteres no válidos
            }, nullValues = "null")
    void validateEmailCases(String email, boolean expectedValid, String message) {
        // Arrange
        usuarioRequest.setEmail(email);
        ActualAssetions(expectedValid,message);
    }


    @ParameterizedTest
    @CsvSource(value={
            "1234abcdHASH,true,''", //Caso válido
            ",false,password es obligatorio", //Caso null
            "'',false,password es obligatorio" // Caso vacio
    },nullValues = "null")
    void validatePasswordHashCase(String passwordHash,boolean expectedValid, String message) {
        //Arranque
        usuarioRequest.setPasswordHash(passwordHash);
        ActualAssetions(expectedValid,message);
    }

    void  ActualAssetions(boolean expectedValid, String message){
        // Act
        Set<ConstraintViolation<UsuariosRequest>> violations = validator.validate(usuarioRequest);

        // Assert
        if (expectedValid) {
            Assertions.assertThat(violations).isEmpty();
        }else{
            Assertions.assertThat(violations).isNotEmpty();
            Assertions.assertThat(violations).anyMatch(v -> v.getMessageTemplate().contains(message));
        }
    }
}