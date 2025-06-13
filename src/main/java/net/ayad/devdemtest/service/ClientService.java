package net.ayad.devdemtest.service;

import lombok.RequiredArgsConstructor;
import net.ayad.devdemtest.dto.ClientDTO;
import net.ayad.devdemtest.mapper.ClientMapper;
import net.ayad.devdemtest.model.Client;
import net.ayad.devdemtest.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public List<ClientDTO> findAll() {
        List<Client> clients = clientRepository.findAll();
        return clientMapper.toDTOList(clients);
    }

    public Optional<ClientDTO> findById(Long id) {
       return clientRepository.findById(id)
               .map(clientMapper::toClientDTO);
    }

    public ClientDTO save(ClientDTO clientDTO) {

        if (clientRepository.existsByEmail(clientDTO.getEmail())) {
            throw new RuntimeException("Un client avec cet email existe déjà: " + clientDTO.getEmail());
        }

        Client client = clientMapper.toClient(clientDTO);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toClientDTO(savedClient);
    }


    public ClientDTO update(Long id, ClientDTO clientDTO) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + id));

        if (!existingClient.getEmail().equals(clientDTO.getEmail()) &&
                clientRepository.existsByEmail(clientDTO.getEmail())) {
            throw new RuntimeException("Un autre client utilise déjà cet email: " + clientDTO.getEmail());
        }

        existingClient.setNom(clientDTO.getNom());
        existingClient.setEmail(clientDTO.getEmail());
        existingClient.setTelephone(clientDTO.getTelephone());

        Client updatedClient = clientRepository.save(existingClient);
        return clientMapper.toClientDTO(updatedClient);
    }

    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client non trouvé avec l'id: " + id);
        }

        // Vérifier s'il y a des devis associés
        long nombreDevis = clientRepository.findById(id)
                .map(client -> client.getDevis().size())
                .orElse(0);

        if (nombreDevis > 0) {
            throw new RuntimeException("Impossible de supprimer le client: il a " + nombreDevis + " devis associé(s)");
        }
        clientRepository.deleteById(id);
    }

    // Supprime un client (force la suppression même avec des devis)
    public void forceDelete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client non trouvé avec l'id: " + id);
        }
        clientRepository.deleteById(id); // Cascade supprimera les devis
    }

}
