package net.ayad.devdemtest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ayad.devdemtest.dto.DevisDTO;
import net.ayad.devdemtest.service.DevisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import net.ayad.devdemtest.model.DevisStatus;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/devis")
@RequiredArgsConstructor
public class DevisController {
    private final DevisService devisService;

    @GetMapping
    public ResponseEntity<List<DevisDTO>> getAllDevis() {
        List<DevisDTO> devis = devisService.findAll();
        return ResponseEntity.ok(devis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DevisDTO> getDevisById(@PathVariable Long id) {
        Optional<DevisDTO> devis = devisService.findById(id);

        if (devis.isPresent()) {
            return ResponseEntity.ok(devis.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<DevisDTO> createDevis(@Valid @RequestBody DevisDTO devisDTO) {
        try {
            // Le service force automatiquement le statut BROUILLON
            DevisDTO savedDevis = devisService.save(devisDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDevis);
        } catch (RuntimeException e) {
            // En cas d'erreur (client non trouvé par exemple)
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DevisDTO> updateDevis(@PathVariable Long id, @Valid @RequestBody DevisDTO devisDTO) {
        try {
            DevisDTO updatedDevis = devisService.update(id, devisDTO);
            return ResponseEntity.ok(updatedDevis);
        } catch (RuntimeException e) {
            // Devis non trouvé ou client non trouvé
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevis(@PathVariable Long id) {
        try {
            devisService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Devis non trouvé
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<DevisDTO>> getDevisByClient(@PathVariable Long clientId) {
        try {
            List<DevisDTO> devis = devisService.findByClientId(clientId);
            return ResponseEntity.ok(devis);
        } catch (RuntimeException e) {
            // Client non trouvé
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/client/{clientId}/count")
    public ResponseEntity<Long> countDevisByClient(@PathVariable Long clientId) {
        long count = devisService.countByClientId(clientId);
        return ResponseEntity.ok(count);
    }

    // ENDPOINTS pour la gestion manuelle des statuts (ACCEPTE, REFUSE, EXPIRE)

    @PatchMapping("/{id}/statut")
    public ResponseEntity<DevisDTO> updateDevisStatus(
            @PathVariable Long id,
            @RequestParam DevisStatus statut) {
        try {
            DevisDTO updatedDevis = devisService.updateStatus(id, statut);
            return ResponseEntity.ok(updatedDevis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<DevisDTO>> getDevisByStatus(@PathVariable DevisStatus statut) {
        try {
            List<DevisDTO> devis = devisService.findByStatus(statut);
            return ResponseEntity.ok(devis);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/statuts")
    public ResponseEntity<DevisStatus[]> getAllStatuts() {
        return ResponseEntity.ok(DevisStatus.values());
    }

    // NOUVEAUX ENDPOINTS pour l'envoi automatique (BROUILLON -> ENVOYE)

    @PostMapping("/{id}/envoyer-sms")
    public ResponseEntity<DevisDTO> envoyerDevisParSMS(
            @PathVariable Long id,
            @RequestParam String numeroTelephone) {
        try {
            DevisDTO devisEnvoye = devisService.envoyerParSMS(id, numeroTelephone);
            return ResponseEntity.ok(devisEnvoye);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/envoyer-email")
    public ResponseEntity<DevisDTO> envoyerDevisParEmail(
            @PathVariable Long id,
            @RequestParam String email) {
        try {
            DevisDTO devisEnvoye = devisService.envoyerParEmail(id, email);
            return ResponseEntity.ok(devisEnvoye);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}