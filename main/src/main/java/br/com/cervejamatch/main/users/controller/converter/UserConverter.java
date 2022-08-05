package br.com.cervejamatch.main.users.controller.converter;

import br.com.cervejamatch.main.users.controller.dto.DefinicoesDeFiltroDTO;
import br.com.cervejamatch.main.users.controller.dto.UserDTO;
import br.com.cervejamatch.main.users.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import br.com.cervejamatch.main.users.model.DefinicoesDeFiltro;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserConverter {
    UserDTO toDto(User user);

    User toModel(UserDTO userDTO);

    DefinicoesDeFiltro definicoesDeFitlroToModel(DefinicoesDeFiltroDTO definicoesDeFiltroDTO);
}
