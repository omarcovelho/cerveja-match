package br.com.cervejamatch.main.promocoes.controller.converter;

import br.com.cervejamatch.main.promocoes.controller.dto.PromocaoDTO;
import br.com.cervejamatch.main.promocoes.model.Promocao;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PromocaoConverter {
    PromocaoDTO toDto(Promocao promocao);

    Promocao toModel(PromocaoDTO promocaoDTO);
}
