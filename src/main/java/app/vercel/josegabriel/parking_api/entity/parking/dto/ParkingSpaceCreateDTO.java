package app.vercel.josegabriel.parking_api.entity.parking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ParkingSpaceCreateDTO(

        @NotBlank
        @Size(min = 4, max = 4)
        String code,
        @NotBlank
        @Pattern(regexp = "LIVRE|OCUPADA")
        String status
) {
}
