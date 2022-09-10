package br.com.cervejamatch.main.matches.repository;

import br.com.cervejamatch.main.matches.domain.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MatchRepository extends MongoRepository<Match, String> {
    List<Match> findByUserId(String userId);
}
