package br.com.cervejamatch.main.promocoes.controller.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

@Builder
@Getter
public class PromocaoDTO {
    private final String id;
    private final String descricao;
    private final String endereco;
    private final String loja;
    private final String criadoPor;
    private final Double precoPorLitro;
    private final GeoJsonPoint localizacao;
    private final List<String> tags;
}
