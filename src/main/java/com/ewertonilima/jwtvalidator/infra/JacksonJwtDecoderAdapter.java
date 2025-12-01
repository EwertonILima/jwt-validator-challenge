package com.ewertonilima.jwtvalidator.infra;

import com.ewertonilima.jwtvalidator.domain.port.JwtDecoderPort;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class JacksonJwtDecoderAdapter implements JwtDecoderPort {

    private static final int TOKEN_PARTS = 3;
    private static final int TOKEN_PAYLOAD_PART = 1;

    private final ObjectMapper objectMapper;

    public JacksonJwtDecoderAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Map<String, Object> decode(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != TOKEN_PARTS) {
                throw new IllegalArgumentException("Token must have 3 parts");
            }

            String payloadPart = parts[TOKEN_PAYLOAD_PART];

            String json = new String(
                    Base64.getUrlDecoder().decode(payloadPart),
                    StandardCharsets.UTF_8
            );

            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT", e);
        }
    }
}
