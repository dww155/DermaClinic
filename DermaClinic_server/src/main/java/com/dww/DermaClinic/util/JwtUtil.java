package com.dww.DermaClinic.util;

import com.dww.DermaClinic.entity.Permission;
import com.dww.DermaClinic.entity.Role;
import com.dww.DermaClinic.entity.User;
import com.dww.DermaClinic.exception.AppException;
import com.dww.DermaClinic.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtUtil {

    @Value("${jwt.secret}")
    String signingKey;

    @Value("${jwt.expiry}")
    long expiry;

    // Generate signed JWT token using Nimbus
    public String generateToken(User user) {
        Date now = new Date();
        Date expiresAt = new Date(now.toInstant().plus(expiry, ChronoUnit.SECONDS).toEpochMilli());

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .issuer("dww")
                .issueTime(now)
                .expirationTime(expiresAt)
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        try {
            JWSSigner signer = new MACSigner(signingKey.getBytes(StandardCharsets.UTF_8));
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Error signing JWT token: {}", e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Cannot sign JWT token");
        }
    }

    // Build space-separated scope string for Spring Security
    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");

        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                joiner.add("ROLE_" + role.getName());

                if (role.getPermissions() != null) {
                    for (Permission permission : role.getPermissions()) {
                        joiner.add(permission.getName());
                    }
                }
            }
        }

        return joiner.toString();
    }

    // Verify token signature and expiration date, throws AppException if invalid
    public SignedJWT verifyToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier verifier = new MACVerifier(signingKey.getBytes(StandardCharsets.UTF_8));
            boolean validSignature = signedJWT.verify(verifier);

            Date now = new Date();
            Date exp = signedJWT.getJWTClaimsSet().getExpirationTime();
            boolean validExp = exp != null && exp.after(now);

            if (!validSignature || !validExp) {
                log.warn("Invalid token: validSignature={}, validExp={}", validSignature, validExp);
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            return signedJWT;
        } catch (ParseException | JOSEException e) {
            log.warn("Failed to parse or verify JWT token: {}", e.getMessage());
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }
}
