package br.com.cervejamatch.main.matches.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class Match {
    private String id;
    private String userId;
    private String promocaoId;
    private ZonedDateTime created;
}
