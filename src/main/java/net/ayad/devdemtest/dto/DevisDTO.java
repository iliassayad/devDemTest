package net.ayad.devdemtest.dto;

import lombok.Builder;
import lombok.Data;
import net.ayad.devdemtest.model.DevisStatus;
import net.ayad.devdemtest.model.Formule;

import java.time.LocalDate;
@Data
@Builder
public class DevisDTO {
    private Long id;

    //Client
    private Long clientId;
    private String clientNom;
    private String clientEmail;
    private String clientTelephone;

    private LocalDate dateDevis;


    private DevisStatus statut = DevisStatus.BROUILLON;

    // Dates départ
    private boolean dateDepartFlexible;
    private LocalDate dateDepart;
    private LocalDate dateDepartMin;
    private LocalDate dateDepartMax;

    // Dates arrivee
    private boolean dateArriveeFlexible;
    private LocalDate dateArrivee;
    private LocalDate dateArriveeMin;
    private LocalDate dateArriveeMax;

    //address de depart
    private String adresseDepart;
    private String villeDepart;

    //address d'arrivee
    private String adresseArrivee;
    private String villeArrivee;

    //Habitation de départ
    private String habitationDepart;

    //Habitation d'arrivee
    private String habitationArrivee;

    private int volume;

    private Formule formule;
    //Prix
    private double prixTTC;
    private double pourcentageArrhes;
    private double montantArrhes;
    //Observation
    private String observation;
}
