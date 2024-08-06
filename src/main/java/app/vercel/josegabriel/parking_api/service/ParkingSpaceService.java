package app.vercel.josegabriel.parking_api.service;

import app.vercel.josegabriel.parking_api.entity.parking.ParkingSpace;
import app.vercel.josegabriel.parking_api.entity.parking.dto.ParkingSpaceCreateDTO;
import app.vercel.josegabriel.parking_api.exception.CodeUniqueViolationException;
import app.vercel.josegabriel.parking_api.exception.EntityNotFoundException;
import app.vercel.josegabriel.parking_api.repository.ParkingSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static app.vercel.josegabriel.parking_api.entity.parking.ParkingSpace.Status.LIVRE;

@Service
@RequiredArgsConstructor
public class ParkingSpaceService {

    private final ParkingSpaceRepository parkingSpaceRepository;

    @Transactional
    public ParkingSpace save(ParkingSpace space) {
        try {
            return parkingSpaceRepository.save(space);
        } catch (
                DataIntegrityViolationException exception) {
            throw new CodeUniqueViolationException(String.format("Código %s já cadastrado", space.getCode()));
        }
    }

    @Transactional(readOnly = true)
    public ParkingSpace findByCode(String code) {
        return parkingSpaceRepository.findParkingSpaceByCode(code).orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));
    }

    @Transactional(readOnly = true)
    public ParkingSpace findFirstAvailable() {
        return parkingSpaceRepository.findFirstByStatus(LIVRE)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma vaga disponível"));
    }

    @Transactional
    public void delete(String code) {
        var park = parkingSpaceRepository.findParkingSpaceByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));
        parkingSpaceRepository.delete(park);
        ResponseEntity.noContent().build();
    }
}
