package br.com.cervejamatch.main.matches;

import br.com.cervejamatch.main.matches.domain.Match;
import br.com.cervejamatch.main.matches.repository.MatchRepository;
import br.com.cervejamatch.main.promocoes.model.Promocao;
import br.com.cervejamatch.main.promocoes.repository.PromocaoRepository;
import br.com.cervejamatch.main.users.model.DefinicoesDeFiltro;
import br.com.cervejamatch.main.users.model.FiltroDePromocao;
import br.com.cervejamatch.main.users.model.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.hamcrest.MockitoHamcrest;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PromocaoMatcherTest {

    @Mock
    PromocaoRepository promocaoRepository;
    @Mock
    MatchRepository matchRepository;
    @InjectMocks
    PromocaoMatcher promocaoMatcher;

    @Test
    public void shouldMatchPromocoesParaUsuario() {

        when(matchRepository.findByUserId("userId1")).thenReturn(Arrays.asList(
            new Match("id1", "userId1", "promocaoId1", ZonedDateTime.now()),
            new Match("id2", "userId1", "promocaoId2", ZonedDateTime.now())
        ));

        when(promocaoRepository.findByLocalizacaoNearAndTagsInAndPrecoPorLitroLessThanEqual(any(), eq(new Distance(0.0, Metrics.KILOMETERS)), eq(Arrays.asList("tag")), eq(0.0))).thenReturn(Arrays.asList(
            new Promocao("promocaoId2", null, null, null, null, null, null, null),
            new Promocao("promocaoId3", null, null, null, null, null, null, null)
        ));

        User usuario = new User("userId1", "nome1", "sobrenome1", "email1@teste.com", new DefinicoesDeFiltro("enedreco", null, Arrays.asList(new FiltroDePromocao("nome",  "tag", 0.0)), 0.0));
        promocaoMatcher.matchParaUser(usuario);

        verify(matchRepository).deleteAllById(MockitoHamcrest.argThat(Matchers.contains("id1")));
        verify(matchRepository).saveAll((List<Match>) MockitoHamcrest.argThat(contains(
            allOf(hasProperty("promocaoId", equalTo("promocaoId3")))
        )));
    }

}