package br.com.cervejamatch.main.promocoes.repository;

import br.com.cervejamatch.main.promocoes.model.Promocao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@Testcontainers
public class PromocaoRepositoryTest {

    @Autowired
    PromocaoRepository promocaoRepository;

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    public void beforeEach() {
        promocaoRepository.saveAll(
            Arrays.asList(
                new Promocao(null, "promocao1", "endereco1", "loja1", "autor1", 10.0, new GeoJsonPoint( -45.89510910217717, -23.20235034129975), Arrays.asList("tags1")),
                new Promocao(null, "promocao2", "endereco2", "loja2", "autor2", 15.0, new GeoJsonPoint( -45.89510910217717, -23.20235034129975), Arrays.asList("tags1")), //preco alto
                new Promocao(null, "promocao3", "endereco3", "loja3", "autor3", 10.0, new GeoJsonPoint( -45.909281203930085, -23.21376148091561), Arrays.asList("tags1")), //localizacao distante
                new Promocao(null, "promocao4", "endereco4", "loja4", "autor4", 10.0, new GeoJsonPoint( -45.89510910217717, -23.20235034129975), Arrays.asList("tags2")) // tags nao batem
            )
        );
    }

    @AfterEach
    public void afterEach() {
        promocaoRepository.deleteAll();
    }

    @Test
    public void shouldBuscarPromocoesBaseadasEmPreferencias() {
        List<Promocao> promocoes = promocaoRepository.findByLocalizacaoNearAndTagsInAndPrecoPorLitroLessThanEqual(new GeoJsonPoint(-45.89217289616783, -23.216616678521365), new Distance(1.7, Metrics.KILOMETERS), Arrays.asList("tags1"), 10.0);
        assertThat(promocoes, hasSize(1));
        assertThat(promocoes.get(0).getDescricao(), equalTo("promocao1"));
    }

}