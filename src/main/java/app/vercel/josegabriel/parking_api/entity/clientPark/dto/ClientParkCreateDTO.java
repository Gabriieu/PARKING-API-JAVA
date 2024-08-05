package app.vercel.josegabriel.parking_api.entity.clientPark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record ClientParkCreateDTO(

        @NotBlank
        @Size(min = 8, max = 8)
        @Pattern(regexp = "[A-Z]{3}-[0-9]{4}", message = "A placa deve seguir o padrão: 'XXX-0000'")
        String plate,

        @NotBlank
        String brand,

        @NotBlank
        String model,

        @NotBlank
        String color,

        @NotBlank
        @Size(min = 11, max = 11)
        @CPF
        @JsonProperty("client_cpf")
        String clientCpf
) {
}
