package br.com.cervejamatch.main.users.controller.dto;

import lombok.Value;

@Value
public class FiltroDePromocaoDTO {
    private final String nome;
    private final String tag;
    private final Double precoPorLitro;
}
