package net.ayad.devdemtest.service;

import net.ayad.devdemtest.dto.DevisDTO;
import net.ayad.devdemtest.mapper.ClientMapper;
import net.ayad.devdemtest.mapper.DevisMapper;
import net.ayad.devdemtest.model.Devis;
import net.ayad.devdemtest.repository.ClientRepository;
import net.ayad.devdemtest.repository.DevisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DevisService {
    private final DevisRepository devisRepository;
    private final ClientRepository clientRepository;
    private final DevisMapper devisMapper;

    @Transactional(readOnly = true)
    public List<DevisDTO> findAll() {
        List<Devis> devisList = devisRepository.findAll();
        return devisMapper.toDTOList(devisList);
    }

    @Transactional(readOnly = true)
    public Optional<DevisDTO> findById(Long id) {
        return devisRepository.findById(id)
                .map(devisMapper::toDevisDTO);
    }

    public DevisDTO save(DevisDTO devisDTO) {
        // Vérifier que le client existe
        if (devisDTO.getClientId() == null) {
            throw new RuntimeException("L'ID du client est obligatoire");
        }

        if (!clientRepository.existsById(devisDTO.getClientId())) {
            throw new RuntimeException("Client non trouvé avec l'id: " + devisDTO.getClientId());
        }

        // S'assurer que l'ID est null pour une création
        devisDTO.setId(null);

        // Le mapper va automatiquement récupérer le client via @AfterMapping
        Devis devis = devisMapper.toDevis(devisDTO);
        Devis savedDevis = devisRepository.save(devis);
        return devisMapper.toDevisDTO(savedDevis);
    }

    public DevisDTO update(Long id, DevisDTO devisDTO) {
        // Vérifier que le devis existe
        Devis existingDevis = devisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Devis non trouvé avec l'id: " + id));

        // Vérifier que le nouveau client existe (si changé)
        if (devisDTO.getClientId() == null) {
            throw new RuntimeException("L'ID du client est obligatoire");
        }

        if (!clientRepository.existsById(devisDTO.getClientId())) {
            throw new RuntimeException("Client non trouvé avec l'id: " + devisDTO.getClientId());
        }

        // Mettre à jour tous les champs
        existingDevis.setDateDevis(devisDTO.getDateDevis());

        // Dates de départ
        existingDevis.setDateDepartFlexible(devisDTO.isDateDepartFlexible());
        existingDevis.setDateDepart(devisDTO.getDateDepart());
        existingDevis.setDateDepartMin(devisDTO.getDateDepartMin());
        existingDevis.setDateDepartMax(devisDTO.getDateDepartMax());

        // Dates d'arrivée
        existingDevis.setDateArriveeFlexible(devisDTO.isDateArriveeFlexible());
        existingDevis.setDateArrivee(devisDTO.getDateArrivee());
        existingDevis.setDateArriveeMin(devisDTO.getDateArriveeMin());
        existingDevis.setDateArriveeMax(devisDTO.getDateArriveeMax());

        // Adresses
        existingDevis.setAdresseDepart(devisDTO.getAdresseDepart());
        existingDevis.setVilleDepart(devisDTO.getVilleDepart());
        existingDevis.setAdresseArrivee(devisDTO.getAdresseArrivee());
        existingDevis.setVilleArrivee(devisDTO.getVilleArrivee());

        // Habitations
        existingDevis.setHabitationDepart(devisDTO.getHabitationDepart());
        existingDevis.setHabitationArrivee(devisDTO.getHabitationArrivee());

        // Autres infos
        existingDevis.setVolume(devisDTO.getVolume());
        existingDevis.setFormule(devisDTO.getFormule());
        existingDevis.setPrixTTC(devisDTO.getPrixTTC());
        existingDevis.setPourcentageArrhes(devisDTO.getPourcentageArrhes());
        existingDevis.setMontantArrhes(devisDTO.getMontantArrhes());
        existingDevis.setObservation(devisDTO.getObservation());

        // Mettre à jour le client si nécessaire
        if (!existingDevis.getClient().getId().equals(devisDTO.getClientId())) {
            existingDevis.setClient(clientRepository.findById(devisDTO.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + devisDTO.getClientId())));
        }

        Devis updatedDevis = devisRepository.save(existingDevis);
        return devisMapper.toDevisDTO(updatedDevis);
    }


    public void deleteById(Long id) {
        if (!devisRepository.existsById(id)) {
            throw new RuntimeException("Devis non trouvé avec l'id: " + id);
        }
        devisRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<DevisDTO> findByClientId(Long clientId) {
        // Vérifier que le client existe
        if (!clientRepository.existsById(clientId)) {
            throw new RuntimeException("Client non trouvé avec l'id: " + clientId);
        }

        List<Devis> devisList = devisRepository.findByClientId(clientId);
        return devisMapper.toDTOList(devisList);
    }



     //Compte le nombre de devis d'un client
    @Transactional(readOnly = true)
    public long countByClientId(Long clientId) {
        return devisRepository.countByClientId(clientId);
    }
}
