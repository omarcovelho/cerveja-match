package br.com.cervejamatch.main.promocoes.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Builder
@Document
public class Promocao {
    private final String id;
    private final String descricao;
    private final String endereco;
    private final String loja;
    private final String criadoPor;
    private final Double precoPorLitro;
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private final GeoJsonPoint localizacao;
    private final List<String> tags;
}
