package app.vercel.josegabriel.parking_api.entity.client.dto;

import app.vercel.josegabriel.parking_api.entity.client.Client;

public record ClientResponseDTO(
        Long id,
        String name,
        String cpf
) {
    public ClientResponseDTO(Client client) {
        this(client.getId(), client.getName(), client.getCpf());
    }
}
