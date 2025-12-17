package net.project.TravelBuddy.security.jwt;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class SignupRequest {

    @NotBlank
    @Size(min = 2, max = 20)
    private String username;

    @NotBlank
    @Size(min = 2, max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;
}
