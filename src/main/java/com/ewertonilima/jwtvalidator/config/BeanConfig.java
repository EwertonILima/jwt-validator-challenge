package com.ewertonilima.jwtvalidator.config;

import com.ewertonilima.jwtvalidator.domain.JwtValidationUseCase;
import com.ewertonilima.jwtvalidator.domain.port.JwtDecoderPort;
import com.ewertonilima.jwtvalidator.infra.out.jwt.JacksonJwtDecoderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class BeanConfig {

    @Bean
    public JwtDecoderPort jwtDecoderPort(ObjectMapper objectMapper) {
        return new JacksonJwtDecoderAdapter(objectMapper);
    }

    @Bean
    public JwtValidationUseCase jwtValidationUseCase(JwtDecoderPort decoderPort) {
        return new JwtValidationUseCase(decoderPort);
    }
}