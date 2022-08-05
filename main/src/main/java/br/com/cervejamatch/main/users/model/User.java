package br.com.cervejamatch.main.users.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@AllArgsConstructor
public class User {

    private String id;
    private String nome;
    private String sobrenome;
    private final String email;
    private DefinicoesDeFiltro definicoesDeFiltro;

    public User updateInfo(String nome, String sobrenome) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        return this;
    }

    public User updateDefinicoesDeFiltro(DefinicoesDeFiltro definicoesDeFiltro) {
        this.definicoesDeFiltro = definicoesDeFiltro;
        return this;
    }
}
