package net.ayad.devdemtest.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "devis")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Devis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @ToString.Exclude
    private Client client;

    private LocalDate dateDevis;

    //date de depart
    private boolean dateDepartFlexible;
    private LocalDate dateDepart;
    private LocalDate dateDepartMin;
    private LocalDate dateDepartMax;

    //date d'arrviee
    private boolean dateArriveeFlexible;
    private LocalDate dateArrivee;
    private LocalDate dateArriveeMin;
    private LocalDate dateArriveeMax;

    //address de depart (address & ville)
    private String adresseDepart;
    private String villeDepart;

    //address de arrivee (address & ville)
    private String adresseArrivee;
    private String villeArrivee;

    //Habitation de départ
    private String habitationDepart;

    //Habitation d'arrivee
    private String habitationArrivee;

    private int volume;

    @Enumerated(EnumType.STRING)
    private Formule formule;

    //Prix
    private double prixTTC;
    private double pourcentageArrhes;
    private double montantArrhes;

    @Column(length = 1000)
    private String observation;

}
