package com.dww.DermaClinic.util;

import com.dww.DermaClinic.entity.Permission;
import com.dww.DermaClinic.entity.Role;
import com.dww.DermaClinic.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey signingKey;
    private final long expiry;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiry}") long expiry
    ) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiry = expiry;
    }

    // Generate access token with subject, userId, roles, permissions claims
    public String generateToken(User user) {
        Date now       = new Date();
        Date expiresAt = new Date(now.getTime() + expiry);

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Set<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId",      user.getId().toString())
                .claim("roles",       roles)
                .claim("permissions", permissions)
                .issuedAt(now)
                .expiration(expiresAt)
                .signWith(signingKey)
                .compact();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(parseClaims(token).get("userId", String.class));
    }

    @SuppressWarnings("unchecked")
    public Set<String> extractRoles(String token) {
        return (Set<String>) parseClaims(token).get("roles", Set.class);
    }

    @SuppressWarnings("unchecked")
    public Set<String> extractPermissions(String token) {
        return (Set<String>) parseClaims(token).get("permissions", Set.class);
    }

    public Date extractExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    // Returns true if signature is valid and token is not expired
    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public boolean isExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
