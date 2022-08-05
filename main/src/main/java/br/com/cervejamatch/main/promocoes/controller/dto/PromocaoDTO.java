package br.com.cervejamatch.main.promocoes.controller.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Builder
@Getter
public class PromocaoDTO {
    private final String id;
    private final String descricao;
    private final String endereco;
    private final String loja;
    private final String criadoPor;
    private final GeoJsonPoint localizacao;
}
