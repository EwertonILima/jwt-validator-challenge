package com.ewertonilima.jwtvalidator.integration;

import com.ewertonilima.jwtvalidator.domain.JwtValidationUseCase;
import com.ewertonilima.jwtvalidator.domain.port.JwtDecoderPort;
import com.ewertonilima.jwtvalidator.infra.out.jwt.JacksonJwtDecoderAdapter;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

class JwtValidationUseCaseIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtDecoderPort decoder = new JacksonJwtDecoderAdapter(objectMapper);
    private final JwtValidationUseCase jwtValidationUseCase = new JwtValidationUseCase(decoder);

    @Test
    void case1_shouldReturnTrue_forValidToken() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
        assertThat(jwtValidationUseCase.isValid(token)).isTrue();
    }

    @Test
    void case2_shouldReturnFalse_forInvalidJwtStructureOrSignature() {
        String token = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
        assertThat(jwtValidationUseCase.isValid(token)).isFalse();
    }

    @Test
    void case3_shouldReturnFalse_whenNameContainsDigits() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";
        assertThat(jwtValidationUseCase.isValid(token)).isFalse();
    }

    @Test
    void case4_shouldReturnFalse_whenMoreThanThreeClaims() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY";
        assertThat(jwtValidationUseCase.isValid(token)).isFalse();
    }
}