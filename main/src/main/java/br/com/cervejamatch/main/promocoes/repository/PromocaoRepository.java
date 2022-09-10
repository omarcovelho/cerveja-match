package br.com.cervejamatch.main.promocoes.repository;

import br.com.cervejamatch.main.promocoes.model.Promocao;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PromocaoRepository extends MongoRepository<Promocao, String> {
    List<Promocao> findByLocalizacaoNearAndTagsInAndPrecoPorLitroLessThanEqual(Point point, Distance max, List<String> tags, Double precoPorLitro);
}
