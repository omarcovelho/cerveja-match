package br.com.cervejamatch.main.users.model;

import lombok.Value;

@Value
public class FiltroDePromocao {
    private final String nome;
    private final String tag;
    private final Double precoPorLitro;
}
