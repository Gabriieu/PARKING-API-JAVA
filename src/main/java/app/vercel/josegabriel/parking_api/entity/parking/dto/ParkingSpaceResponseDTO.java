package app.vercel.josegabriel.parking_api.entity.parking.dto;

import app.vercel.josegabriel.parking_api.entity.parking.ParkingSpace;

public record ParkingSpaceResponseDTO(

        Long id,
        String code,
        String status
) {
    public ParkingSpaceResponseDTO(ParkingSpace parkingSpace) {
        this(parkingSpace.getId(), parkingSpace.getCode(), parkingSpace.getStatus().name());
    }
}
