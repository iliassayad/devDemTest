package net.ayad.devdemtest.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(unique = true)
    private String email;

    private String telephone;
    @Builder.Default
    private LocalDate dateCreation = LocalDate.now();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Devis> devis = new ArrayList<>();
}
