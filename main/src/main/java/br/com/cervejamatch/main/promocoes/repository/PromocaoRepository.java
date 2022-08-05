package br.com.cervejamatch.main.promocoes.repository;

import br.com.cervejamatch.main.promocoes.model.Promocao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PromocaoRepository extends MongoRepository<Promocao, String> {
}
