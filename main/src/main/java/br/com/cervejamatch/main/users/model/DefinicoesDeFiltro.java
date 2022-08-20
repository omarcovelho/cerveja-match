package br.com.cervejamatch.main.users.model;

import lombok.Value;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

@Value
public class DefinicoesDeFiltro {
    private final String endereco;
    private final GeoJsonPoint localizacao;
    private final List<FiltroDePromocao> filtros;
    private final Double distancia;
}
