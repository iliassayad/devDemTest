package net.ayad.devdemtest.repository;

import net.ayad.devdemtest.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByEmail(String email);
}
