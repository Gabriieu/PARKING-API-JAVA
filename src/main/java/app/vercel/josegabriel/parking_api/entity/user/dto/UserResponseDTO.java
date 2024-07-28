package app.vercel.josegabriel.parking_api.entity.user.dto;

import app.vercel.josegabriel.parking_api.entity.user.User;

public record UserResponseDTO(
        Long id,
        String username,
        String role
) {
    public UserResponseDTO(User user) {
        this(user.getId(),
                user.getUsername(),
                user.getRole().name().substring("ROLE_".length()));
    }
}
