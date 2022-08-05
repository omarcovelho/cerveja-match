package br.com.cervejamatch.main.promocoes.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@RequiredArgsConstructor
@Getter
@Builder
public class Promocao {
    private final String id;
    private final String descricao;
    private final String endereco;
    private final String loja;
    private final String criadoPor;
    private final GeoJsonPoint localizacao;
}
