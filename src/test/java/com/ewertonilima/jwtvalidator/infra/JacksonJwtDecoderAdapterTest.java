package com.ewertonilima.jwtvalidator.infra;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JacksonJwtDecoderAdapterTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JacksonJwtDecoderAdapter adapter = new JacksonJwtDecoderAdapter(objectMapper);

    @Test
    void decode_shouldReturnClaimsMap_whenTokenIsValid() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        Map<String, Object> claims = adapter.decode(token);

        assertThat(claims)
                .containsEntry("Role", "Admin")
                .containsEntry("Seed", "7841")
                .containsEntry("Name", "Toninho Araujo");

        assertThat(claims).hasSize(3);
    }

    @Test
    void decode_shouldThrowException_whenTokenDoesNotHaveThreeParts() {
        String tokenNull = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        assertThatThrownBy(() -> adapter.decode(tokenNull))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid JWT");
    }

    @Test
    void decode_shouldThrowException_whenPayloadIsNotValidJson() {
        String headerJson = "{\"alg\":\"HS256\"}";
        String invalidJsonPayload = "not-a-json";

        String headerB64 = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));

        String payloadB64 = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(invalidJsonPayload.getBytes(StandardCharsets.UTF_8));

        String token = headerB64 + "." + payloadB64 + ".signature";

        assertThatThrownBy(() -> adapter.decode(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid JWT")
                .hasCauseInstanceOf(Exception.class);
    }
}