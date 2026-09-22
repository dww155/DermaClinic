package com.dww.DermaClinic.config;

import com.dww.DermaClinic.exception.AppException;
import com.dww.DermaClinic.util.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomJwtDecoder implements JwtDecoder {

    @NonFinal
    @Value("${jwt.secret}")
    String signingKey;

    @NonFinal
    NimbusJwtDecoder nimbusJwtDecoder;

    JwtUtil jwtUtil;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            jwtUtil.verifyToken(token);
        } catch (AppException e) {
            throw new BadJwtException(e.getMessage(), e);
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signingKey.getBytes(StandardCharsets.UTF_8), "HS256");

            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
