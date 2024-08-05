package app.vercel.josegabriel.parking_api.service;

import app.vercel.josegabriel.parking_api.entity.client.Client;
import app.vercel.josegabriel.parking_api.exception.CpfUniqueViolationException;
import app.vercel.josegabriel.parking_api.exception.EntityNotFoundException;
import app.vercel.josegabriel.parking_api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public Client save(Client client) {
        try {
            return clientRepository.save(client);
        } catch (
                DataIntegrityViolationException exception) {
            throw new CpfUniqueViolationException("Usuário já cadastrado no sistema");
        }
    }

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable page) {
        return clientRepository.findAll(page);
    }

    @Transactional(readOnly = true)
    public Client findByUserId(Long id) {
        return clientRepository.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    @Transactional(readOnly = true)
    public Client findByCpf(String cpf) {
        return clientRepository.findClientByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cliente com CPF %s não encontrado", cpf)));
    }
}
