package net.ayad.devdemtest.mapper;

import net.ayad.devdemtest.dto.ClientDTO;
import net.ayad.devdemtest.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    //Client Entity --> Client DTO
    @Mapping(target = "nombreDevis", expression = "java(client.getDevis() != null ? client.getDevis().size() : 0)")
    ClientDTO toClientDTO(Client client);

    //Client DTO --> Client Entity
    @Mapping(target = "devis", ignore = true)
    @Mapping(target = "dateCreation", expression = "java(clientDTO.getDateCreation() != null ? clientDTO.getDateCreation() : java.time.LocalDate.now())") // ✅ AJOUT
    Client toClient(ClientDTO clientDTO);


    // Liste Entity --> Liste DTO
    List<ClientDTO> toDTOList(List<Client> clients);

    // Liste DTO  --> Liste Entity
    List<Client> toEntityList(List<ClientDTO> clientDTOs);
}
