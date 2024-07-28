package app.vercel.josegabriel.parking_api.entity.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
        @Email(regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "Invalid email")
        @Email
        String username,
        @NotBlank
        String password
) {
}
