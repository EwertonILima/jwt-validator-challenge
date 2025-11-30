package com.ewertonilima.jwtvalidator.domain;

import com.ewertonilima.jwtvalidator.domain.port.JwtDecoderPort;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtValidationUseCaseTest {

    @Test
    void case1_shouldReturnTrueForValidToken() {
        JwtDecoderPort stubDecoder = _ -> Map.of(
                "Role", "Admin",
                "Seed", "7841",
                "Name", "Toninho Araujo"
        );

        JwtValidationUseCase jwtValidationUseCase = new JwtValidationUseCase(stubDecoder);

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        boolean result = jwtValidationUseCase.isValid(token);

        assertThat(result).isTrue();
    }

    @Test
    void case2_shouldReturnFalseForInvalidJwt() {
        JwtDecoderPort stubDecoder = _ -> null;

        JwtValidationUseCase jwtValidationUseCase = new JwtValidationUseCase(stubDecoder);

        String token = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        boolean result = jwtValidationUseCase.isValid(token);

        assertThat(result).isFalse();
    }

    @Test
    void case3_shouldReturnFalseWhenNameHasDigits() {
        JwtDecoderPort stubDecoder = _ -> Map.of(
                "Role", "External",
                "Seed", "72341",
                "Name", "M4ria Olivia"
        );

        JwtValidationUseCase validationUseCase = new JwtValidationUseCase(stubDecoder);

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";

        boolean result = validationUseCase.isValid(token);

        assertThat(result).isFalse();
    }

    @Test
    void case4_shouldReturnFalseWhenMoreThanThreeClaims() {
        JwtDecoderPort stubDecoder = _ -> Map.of(
                "Role", "Member",
                "Org", "BR",
                "Seed", "14627",
                "Name", "Valdir Aranha"
        );

        JwtValidationUseCase jwtValidationUseCase = new JwtValidationUseCase(stubDecoder);

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY";

        boolean result = jwtValidationUseCase.isValid(token);

        assertThat(result).isFalse();
    }
}