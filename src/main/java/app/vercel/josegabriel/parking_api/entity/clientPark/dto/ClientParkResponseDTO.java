package app.vercel.josegabriel.parking_api.entity.clientPark.dto;

import app.vercel.josegabriel.parking_api.entity.clientPark.ClientPark;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClientParkResponseDTO(

        Long id,
        String receipt,
        String plate,
        String brand,
        String model,
        String color,
        @JsonProperty("client_cpf")
        String clientCpf,
        @JsonProperty("parking_space_code")
        String parkingSpaceCode,
        @JsonProperty("entry_time")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime entryTime,
        @JsonProperty("exit_time")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime exitTime,
        String value,
        String discount
) {
    public ClientParkResponseDTO(ClientPark dto,
                                 @Nullable String exitTime,
                                 @Nullable String value,
                                 @Nullable String discount) {
        this(dto.getId(),
                dto.getReceipt(),
                dto.getPlate(),
                dto.getBrand(),
                dto.getModel(),
                dto.getColor(),
                dto.getClient().getCpf(),
                dto.getParkingSpace().getCode(),
                dto.getEntryTime(),
                exitTime != null ? LocalDateTime.parse(exitTime) : null,
                value,
                discount);
    }

    public ClientParkResponseDTO(ClientPark clientPark) {
        this(clientPark,
                clientPark.getExitTime() != null ? clientPark.getExitTime().toString() : null,
                clientPark.getValue() != null ? clientPark.getValue().toString() : null,
                clientPark.getDiscount() != null ? clientPark.getDiscount().toString() : null);
    }
}
