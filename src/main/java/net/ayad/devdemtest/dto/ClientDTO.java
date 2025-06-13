package net.ayad.devdemtest.dto;

import lombok.Builder;
import lombok.Data;


import java.time.LocalDate;
@Data
@Builder
public class ClientDTO {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private LocalDate dateCreation;
    private int nombreDevis;
}
