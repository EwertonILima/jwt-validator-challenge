package com.ewertonilima.jwtvalidator.infra.in.web;

import com.ewertonilima.jwtvalidator.domain.JwtValidationUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jwt")
public class JwtValidationController {

    private static final Logger log = LoggerFactory.getLogger(JwtValidationController.class);

    private final JwtValidationUseCase jwtValidationUseCase;

    public JwtValidationController(JwtValidationUseCase jwtValidationUseCase) {
        this.jwtValidationUseCase = jwtValidationUseCase;
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam("token") String token) {
        if (token == null || token.isBlank()) {
            log.info("JWT validation failed: empty token");
            return ResponseEntity.ok("falso");
        }

        boolean valid = jwtValidationUseCase.isValid(token);
        log.info("JWT validation result: {}", valid);

        String result = valid ? "verdadeiro" : "falso";
        return ResponseEntity.ok((result));
    }
}
