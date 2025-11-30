package com.ewertonilima.jwtvalidator.domain.port;

import java.util.Map;

public interface JwtDecoderPort {
    Map<String, Object> decode(String token);
}