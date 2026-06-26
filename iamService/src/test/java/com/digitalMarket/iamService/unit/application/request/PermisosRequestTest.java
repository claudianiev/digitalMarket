package com.digitalMarket.iamService.unit.application.request;

import com.digitalMarket.iamService.application.dto.request.PermisosRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import java.util.stream.Stream;

@ActiveProfiles("testcontainers")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PermisosRequestTest {

    private Validator validator;
    private PermisosRequest permisosRequest;

    @BeforeEach
    void setUp() {
        validator = createValidator();
        Assertions.assertThat(validator).isNotNull();
        initRolRequest();
    }

    void initRolRequest() {
        permisosRequest = new PermisosRequest();
        permisosRequest.setNombre("Asignar Roles");
        permisosRequest.setDescripcion("Asigna roles a usuarios");
    }

    private Validator createValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    @ParameterizedTest
    @MethodSource("caseRoles")
    void validateNombreCase(String nombre, boolean expectedValid, String message) {
        //Arranque
        permisosRequest.setNombre(nombre);
        ActualAssetions(expectedValid,message);
    }

    void  ActualAssetions(boolean expectedValid, String message){
        // Act
        Set<ConstraintViolation<PermisosRequest>> violations = validator.validate(permisosRequest);

        // Assert
        if (expectedValid) {
            Assertions.assertThat(violations).isEmpty();
        }else{
            Assertions.assertThat(violations).isNotEmpty();
            Assertions.assertThat(violations).anyMatch(v -> v.getMessageTemplate().contains(message));
        }
    }

    private static Stream<Arguments> caseRoles() {
        return Stream.of(
                Arguments.of("Asignar Roles", true, ""),
                Arguments.of("a".repeat(50), true, ""), // límite válido
                Arguments.of("a".repeat(51), false, "El tamaño máximo es de 50 caracteres."), // inválido
                Arguments.of(null, false, "No puede ir campo null"), // válido porque @Size permite null
                Arguments.of("", false, "Nombre del permiso requerido")    // válido porque "" tiene tamaño 0
        );
    }
}