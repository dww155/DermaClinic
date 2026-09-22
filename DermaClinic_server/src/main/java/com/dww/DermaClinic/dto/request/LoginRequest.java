package com.dww.DermaClinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {

    @NotBlank(message = "INVALID_REQUEST")
    @Pattern(regexp = "^[0-9]{9,15}$", message = "INVALID_REQUEST")
    String phoneNumber;

    @NotBlank(message = "INVALID_REQUEST")
    String password;
}
