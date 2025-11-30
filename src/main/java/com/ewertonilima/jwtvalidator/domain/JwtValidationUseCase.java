package com.ewertonilima.jwtvalidator.domain;

import com.ewertonilima.jwtvalidator.domain.vo.JwtClaims;
import com.ewertonilima.jwtvalidator.domain.port.JwtDecoderPort;

import java.util.Map;
import java.util.Set;

public class JwtValidationUseCase {

    private static final int CLAIM_LIMIT_SIZE = 3;
    private static final String CLAIM_NAME = "Name";
    private static final String CLAIM_ROLE = "Role";
    private static final String CLAIM_SEED = "Seed";

    private static final Set<String> VALID_ROLES = Set.of("Admin", "Member", "External");
    private static final int MAX_NAME_LENGTH = 256;

    private final JwtDecoderPort jwtDecoderPort;

    public JwtValidationUseCase(JwtDecoderPort jwtDecoderPort) {
        this.jwtDecoderPort = jwtDecoderPort;
    }

    public boolean isValid(String token) {
        JwtClaims claims = validateAndExtractClaims(token);
        if (claims == null) {
            return false;
        }
        return isPrime(claims.seed());
    }

    private JwtClaims validateAndExtractClaims(String token) {
        Map<String, Object> rawClaims = decodeOrNull(token);
        if (rawClaims == null) {
            return null;
        }

        if (!hasExactlyExpectedClaims(rawClaims)) {
            return null;
        }

        String name = extractAndValidateName(rawClaims);
        if (name == null) {
            return null;
        }

        String role = extractAndValidateRole(rawClaims);
        if (role == null) {
            return null;
        }

        Long seed = extractAndValidateSeed(rawClaims);
        if (seed == null) {
            return null;
        }

        return new JwtClaims(name, role, seed);
    }

    private Map<String, Object> decodeOrNull(String token) {
        try {
            return jwtDecoderPort.decode(token);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean hasExactlyExpectedClaims(Map<String, Object> claims) {
        if (claims.size() != CLAIM_LIMIT_SIZE) {
            return false;
        }
        return claims.containsKey(CLAIM_NAME)
                && claims.containsKey(CLAIM_ROLE)
                && claims.containsKey(CLAIM_SEED);
    }

    private String extractAndValidateName(Map<String, Object> claims) {
        Object nameObj = claims.get(CLAIM_NAME);
        if (!(nameObj instanceof String name)) {
            return null;
        }

        if (name.isBlank()) {
            return null;
        }

        if (name.length() > MAX_NAME_LENGTH) {
            return null;
        }

        if (name.matches(".*\\d.*")) {
            return null;
        }

        return name;
    }

    private String extractAndValidateRole(Map<String, Object> claims) {
        Object roleObj = claims.get(CLAIM_ROLE);
        if (!(roleObj instanceof String role)) {
            return null;
        }

        if (!VALID_ROLES.contains(role)) {
            return null;
        }

        return role;
    }

    private Long extractAndValidateSeed(Map<String, Object> claims) {
        Object seedObj = claims.get(CLAIM_SEED);
        if (seedObj == null) {
            return null;
        }

        try {
            long value;

            if (seedObj instanceof String s) {
                value = Long.parseLong(s);
            } else if (seedObj instanceof Number n) {
                value = n.longValue();
            } else {
                return null;
            }

            return value;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean isPrime(long n) {
        if (n <= 1) {
            return false;
        }

        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }
}