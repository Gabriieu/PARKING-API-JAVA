package app.vercel.josegabriel.parking_api.service;

import app.vercel.josegabriel.parking_api.entity.client.Client;
import app.vercel.josegabriel.parking_api.entity.clientPark.ClientPark;
import app.vercel.josegabriel.parking_api.entity.parking.ParkingSpace;
import app.vercel.josegabriel.parking_api.util.ParkingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParkService {

    private final ClientParkService clientParkService;
    private final ClientService clientService;
    private final ParkingSpaceService parkingSpaceService;

    @Transactional
    public ClientPark checkIn(ClientPark clientPark) {
        Client client = clientService.findByCpf(clientPark.getClient().getCpf());
        clientPark.setClient(client);

        ParkingSpace parkingSpace = parkingSpaceService.findFirstAvailable();
        parkingSpace.setStatus(ParkingSpace.Status.OCUPADA);
        clientPark.setParkingSpace(parkingSpace);

        clientPark.setEntryTime(LocalDateTime.now());
        clientPark.setReceipt(ParkingUtils.generateReceipt(clientPark.getPlate()));

        return clientParkService.save(clientPark);
    }

    @Transactional
    public ClientPark checkOut(String receipt) {
        var exitTime = LocalDateTime.now();
        ClientPark clientPark = clientParkService.findByReceipt(receipt);

        BigDecimal value = ParkingUtils.calculateValue(clientPark.getEntryTime(), exitTime);

        long totalParkingVisits = clientParkService.countParkByClientCpf(clientPark.getClient().getCpf());
        BigDecimal discount = ParkingUtils.calculateDiscount(value, totalParkingVisits);

        clientPark.setDiscount(discount);
        clientPark.setValue(value);
        clientPark.setExitTime(exitTime);
        clientPark.getParkingSpace().setStatus(ParkingSpace.Status.LIVRE);

        return clientParkService.save(clientPark);
    }
}
