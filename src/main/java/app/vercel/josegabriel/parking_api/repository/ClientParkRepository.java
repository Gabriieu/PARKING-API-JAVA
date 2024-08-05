package app.vercel.josegabriel.parking_api.repository;

import app.vercel.josegabriel.parking_api.entity.clientPark.ClientPark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientParkRepository extends JpaRepository<ClientPark, Long> {

    Optional<ClientPark> findByReceiptAndExitTimeIsNull(String receipt);

    int countByClientCpfAndExitTimeIsNotNull(String cpf);

    Page<ClientPark> findAllByClientCpf(String cpf, Pageable page);

    Page<ClientPark> findAllByClientUserId(Long id, Pageable page);
}
