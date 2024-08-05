package app.vercel.josegabriel.parking_api.service;

import app.vercel.josegabriel.parking_api.entity.clientPark.ClientPark;
import app.vercel.josegabriel.parking_api.exception.EntityNotFoundException;
import app.vercel.josegabriel.parking_api.repository.ClientParkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientParkService {

    private final ClientParkRepository clientParkRepository;

    @Transactional
    public ClientPark save(ClientPark clientPark) {
        return clientParkRepository.save(clientPark);
    }

    @Transactional(readOnly = true)
    public ClientPark findByReceipt(String receipt) {
        return clientParkRepository.findByReceiptAndExitTimeIsNull(receipt)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Recibo %s não encontrado ou check-out já realizado", receipt)));
    }


    @Transactional(readOnly = true)
    public long countParkByClientCpf(String cpf) {
        return clientParkRepository.countByClientCpfAndExitTimeIsNotNull(cpf);
    }


    @Transactional(readOnly = true)
    public Page<ClientPark> findAllByClientCpf(String cpf, Pageable page) {
        return clientParkRepository.findAllByClientCpf(cpf, page);
    }


    @Transactional(readOnly = true)
    public Page<ClientPark> findAllParksByUserId(Long id, Pageable page) {
        return clientParkRepository.findAllByClientUserId(id, page);
    }
}
