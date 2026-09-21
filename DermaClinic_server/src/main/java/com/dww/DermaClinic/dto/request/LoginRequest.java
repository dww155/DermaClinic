package com.dww.DermaClinic.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {

    @NotBlank(message = "INVALID_REQUEST")
    @Email(message = "INVALID_REQUEST")
    String email;

    @NotBlank(message = "INVALID_REQUEST")
    String password;
}
