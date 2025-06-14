package net.ayad.devdemtest.mapper;

import net.ayad.devdemtest.dto.DevisDTO;
import net.ayad.devdemtest.model.Client;
import net.ayad.devdemtest.model.Devis;
import net.ayad.devdemtest.repository.ClientRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class DevisMapper {

    @Autowired
    protected ClientRepository clientRepository;

    //Devis entity --> Devis DTO
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.nom", target = "clientNom")
    @Mapping(source = "client.email", target = "clientEmail")
    @Mapping(source = "client.telephone", target = "clientTelephone")
    public abstract DevisDTO toDevisDTO(Devis devis);

    //Devis DTO --> Devis entity
    @Mapping(target = "client", ignore = true)
    public abstract Devis toDevis(DevisDTO devisDTO);

    // Liste Entity --> Liste DTO
    public abstract List<DevisDTO> toDTOList(List<Devis> devisList);

    // Liste DTO --> Liste Entity
    public abstract List<Devis> toEntityList(List<DevisDTO> devisDTOs);

    //récupèration et assigner le client
    @AfterMapping
    protected void setClient(@MappingTarget Devis devis, DevisDTO devisDTO){
        if (devisDTO.getClientId() != null) {
            Client client = clientRepository.findById(devisDTO.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + devisDTO.getClientId()));
            devis.setClient(client);
        }
    }
}
