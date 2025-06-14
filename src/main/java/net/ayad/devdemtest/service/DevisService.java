package net.ayad.devdemtest.service;

import net.ayad.devdemtest.dto.DevisDTO;
import net.ayad.devdemtest.mapper.DevisMapper;
import net.ayad.devdemtest.model.Devis;
import net.ayad.devdemtest.model.DevisStatus;
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

        // MODIFICATION : Forcer le statut BROUILLON pour un nouveau devis
        devisDTO.setStatut(DevisStatus.BROUILLON);

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

        // MODIFICATION : Contrôler les changements de statut autorisés
        if (devisDTO.getStatut() != null) {
            validateStatusChange(existingDevis.getStatut(), devisDTO.getStatut());
            existingDevis.setStatut(devisDTO.getStatut());
        }

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

    // MODIFICATION : Changement de statut avec validation
    public DevisDTO updateStatus(Long id, DevisStatus newStatus) {
        Devis devis = devisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Devis non trouvé avec l'id: " + id));

        validateStatusChange(devis.getStatut(), newStatus);
        devis.setStatut(newStatus);
        Devis updatedDevis = devisRepository.save(devis);
        return devisMapper.toDevisDTO(updatedDevis);
    }

    @Transactional(readOnly = true)
    public List<DevisDTO> findByStatus(DevisStatus status) {
        List<Devis> devisList = devisRepository.findByStatut(status);
        return devisMapper.toDTOList(devisList);
    }

    // NOUVELLES MÉTHODES : Gestion automatique lors de l'envoi

    /**
     * Envoyer le devis par SMS - Change automatiquement le statut vers ENVOYE
     */
    public DevisDTO envoyerParSMS(Long devisId, String numeroTelephone) {
        Devis devis = devisRepository.findById(devisId)
                .orElseThrow(() -> new RuntimeException("Devis non trouvé avec l'id: " + devisId));

        // Vérifier que le devis peut être envoyé
        if (devis.getStatut() != DevisStatus.BROUILLON) {
            throw new RuntimeException("Seuls les devis en brouillon peuvent être envoyés");
        }

        // TODO: Implémenter l'envoi SMS réel ici
        // smsService.envoyerDevis(devis, numeroTelephone);

        // Changer automatiquement le statut vers ENVOYE
        devis.setStatut(DevisStatus.ENVOYE);
        Devis updatedDevis = devisRepository.save(devis);

        return devisMapper.toDevisDTO(updatedDevis);
    }

    /**
     * Envoyer le devis par Email - Change automatiquement le statut vers ENVOYE
     */
    public DevisDTO envoyerParEmail(Long devisId, String email) {
        Devis devis = devisRepository.findById(devisId)
                .orElseThrow(() -> new RuntimeException("Devis non trouvé avec l'id: " + devisId));

        // Vérifier que le devis peut être envoyé
        if (devis.getStatut() != DevisStatus.BROUILLON) {
            throw new RuntimeException("Seuls les devis en brouillon peuvent être envoyés");
        }

        // TODO: Implémenter l'envoi Email réel ici
        // emailService.envoyerDevis(devis, email);

        // Changer automatiquement le statut vers ENVOYE
        devis.setStatut(DevisStatus.ENVOYE);
        Devis updatedDevis = devisRepository.save(devis);

        return devisMapper.toDevisDTO(updatedDevis);
    }

    /**
     * Valider les changements de statut autorisés
     */
    private void validateStatusChange(DevisStatus currentStatus, DevisStatus newStatus) {
        // Si c'est le même statut, pas de validation nécessaire
        if (currentStatus == newStatus) {
            return;
        }

        switch (currentStatus) {
            case BROUILLON:
                // Depuis BROUILLON : seul le système peut passer à ENVOYE (via SMS/Email)
                if (newStatus != DevisStatus.ENVOYE) {
                    throw new RuntimeException("Un devis en brouillon ne peut être modifié que par envoi SMS/Email");
                }
                break;

            case ENVOYE:
                // Depuis ENVOYE : l'utilisateur peut changer vers ACCEPTE, REFUSE, ou EXPIRE
                if (newStatus != DevisStatus.ACCEPTE &&
                        newStatus != DevisStatus.REFUSE &&
                        newStatus != DevisStatus.EXPIRE) {
                    throw new RuntimeException("Un devis envoyé ne peut être que accepté, refusé ou expiré");
                }
                break;

            case ACCEPTE:
            case REFUSE:
            case EXPIRE:
                // Les statuts finaux ne peuvent plus être modifiés
                throw new RuntimeException("Un devis " + currentStatus.getLabel().toLowerCase() + " ne peut plus être modifié");

            default:
                throw new RuntimeException("Statut inconnu: " + currentStatus);
        }
    }
}