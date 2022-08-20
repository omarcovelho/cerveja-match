package br.com.cervejamatch.main.users.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDTO {
    private final String id;
    private final String nome;
    private final String sobrenome;
    private final String email;
    private final DefinicoesDeFiltroDTO definicoesDeFiltro;
}
