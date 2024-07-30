package app.vercel.josegabriel.parking_api.repository;

import app.vercel.josegabriel.parking_api.entity.parking.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {

    Optional<ParkingSpace> findParkingSpaceByCode(String code);
}
