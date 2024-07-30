package app.vercel.josegabriel.parking_api.entity.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record ClientCreateDTO(
        @NotBlank
        @Size(min = 3, max = 100)
        String name,
        @CPF
        @Size(min = 11, max = 11)
        String cpf
) {
}
