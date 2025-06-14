package net.ayad.devdemtest.repository;

import net.ayad.devdemtest.model.Devis;
import net.ayad.devdemtest.model.DevisStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevisRepository extends JpaRepository<Devis, Long> {

    List<Devis> findByClientId(Long clientId);

    long countByClientId(Long clientId);

    List<Devis> findByStatut(DevisStatus status);

    long countByStatut(DevisStatus statut);
}
