package com.dww.DermaClinic.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntrospectResponse {

    boolean valid;
    String userId;
    String email;
    String scope;
    Date expiresAt;
}
