package com.dww.DermaClinic.service;

import com.dww.DermaClinic.dto.request.IntrospectRequest;
import com.dww.DermaClinic.dto.request.LoginRequest;
import com.dww.DermaClinic.dto.response.IntrospectResponse;
import com.dww.DermaClinic.dto.response.LoginResponse;
import com.dww.DermaClinic.entity.User;
import com.dww.DermaClinic.exception.AppException;
import com.dww.DermaClinic.exception.ErrorCode;
import com.dww.DermaClinic.repository.UserRepository;
import com.dww.DermaClinic.util.JwtUtil;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);

        if (!user.isEnabled())
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        String token = jwtUtil.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        // verifyToken throws AppException(UNAUTHENTICATED) if invalid or expired
        SignedJWT signedJWT = jwtUtil.verifyToken(request.getToken());

        try {
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            return IntrospectResponse.builder()
                    .valid(true)
                    .userId(claims.getSubject())
                    .email(claims.getStringClaim("email"))
                    .scope(claims.getStringClaim("scope"))
                    .expiresAt(claims.getExpirationTime())
                    .build();

        } catch (ParseException e) {
            log.error("Failed to parse JWT claims: {}", e.getMessage());
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }
}
