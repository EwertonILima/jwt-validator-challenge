package com.ewertonilima.jwtvalidator.infra.in.web;


import com.ewertonilima.jwtvalidator.domain.JwtValidationUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JwtValidationControllerTest {

    @Test
    void shouldReturnVerdadeiroWhenUseCaseReturnsTrue() {
        JwtValidationUseCase jwtValidationUseCase = mock(JwtValidationUseCase.class);
        JwtValidationController jwtValidationController = new JwtValidationController(jwtValidationUseCase);

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        when(jwtValidationUseCase.isValid(token)).thenReturn(true);

        ResponseEntity<String> response = jwtValidationController.validate(token);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo("verdadeiro");

        verify(jwtValidationUseCase).isValid(token);
    }

    @Test
    void shouldReturnFalsoWhenUseCaseReturnsFalse() {
        JwtValidationUseCase jwtValidationUseCase = mock(JwtValidationUseCase.class);
        JwtValidationController jwtValidationController = new JwtValidationController(jwtValidationUseCase);

        String token = "invalid-token";
        when(jwtValidationUseCase.isValid(token)).thenReturn(false);

        ResponseEntity<String> response = jwtValidationController.validate(token);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo("falso");

        verify(jwtValidationUseCase).isValid(token);
    }

    @Test
    void shouldReturnFalsoWhenTokenIsEmpty() {
        JwtValidationUseCase jwtValidationUseCase = mock(JwtValidationUseCase.class);
        JwtValidationController jwtValidationController = new JwtValidationController(jwtValidationUseCase);

        String emptyToken = "";

        ResponseEntity<String> response = jwtValidationController.validate(emptyToken);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo("falso");

        verifyNoInteractions(jwtValidationUseCase);
    }
}