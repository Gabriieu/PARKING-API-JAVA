package app.vercel.josegabriel.parking_api.repository;

import app.vercel.josegabriel.parking_api.entity.client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Page<Client> findAll(Pageable pageable);

    Optional<Client> findByUserId(Long id);

    Optional<Client> findClientByCpf(String cpf);
}
