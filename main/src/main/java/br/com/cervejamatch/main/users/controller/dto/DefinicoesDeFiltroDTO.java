package br.com.cervejamatch.main.users.controller.dto;

import lombok.Value;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

@Value
public class DefinicoesDeFiltroDTO {
    private final String endereco;
    private final GeoJsonPoint localizacao;
    private final List<FiltroDePromocaoDTO> filtros;
    private final Double distancia;
}
