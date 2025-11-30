package com.ewertonilima.jwtvalidator.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtValidationUseCaseTest {

    private final JwtValidationUseCase validator = new JwtValidationUseCase ();

    @Test
    void case1_shouldReturnTrueForValidToken() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        boolean result = validator.isValid(token);

        assertThat(result).isTrue();
    }
}