package br.com.cervejamatch.main.matches;

import br.com.cervejamatch.main.matches.domain.Match;
import br.com.cervejamatch.main.matches.repository.MatchRepository;
import br.com.cervejamatch.main.promocoes.model.Promocao;
import br.com.cervejamatch.main.promocoes.repository.PromocaoRepository;
import br.com.cervejamatch.main.users.model.DefinicoesDeFiltro;
import br.com.cervejamatch.main.users.model.FiltroDePromocao;
import br.com.cervejamatch.main.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class PromocaoMatcher {

    private final PromocaoRepository promocaoRepository;
    private final MatchRepository matchRepository;

    public void matchParaUser(User user) {
        List<Promocao> promocoesEncontradas = encontrarPromocoesPara(user);
        List<String> promocoesEncontradasIds = promocoesEncontradas.stream().collect(mapping(Promocao::getId, toList()));

        List<Match> matchesExistentes = matchRepository.findByUserId(user.getId());
        List<String> matchesPromocoesIds = matchesExistentes.stream().collect(mapping(Match::getPromocaoId, toList()));

        List<Match> novosMatches = promocoesEncontradas.stream()
                .filter(p -> !matchesPromocoesIds.contains(p.getId()))
                .map(p -> new Match(null, user.getId(), p.getId(), ZonedDateTime.now()))
                .collect(toList());

        List<String> matchesParaRemover = matchesExistentes.stream()
                .filter(m -> !promocoesEncontradasIds.contains(m.getPromocaoId()))
                .collect(mapping(Match::getId, toList()));

        matchRepository.deleteAllById(matchesParaRemover);
        matchRepository.saveAll(novosMatches);
    }

    private List<Promocao> encontrarPromocoesPara(User user) {
        DefinicoesDeFiltro preferencias = user.getDefinicoesDeFiltro();
        List<FiltroDePromocao> filtros = preferencias.getFiltros();
        List<Promocao> promocoesEncontradas = filtros.stream()
                .map(f -> promocaoRepository.findByLocalizacaoNearAndTagsInAndPrecoPorLitroLessThanEqual(preferencias.getLocalizacao(), new Distance(preferencias.getDistancia(), Metrics.KILOMETERS), Arrays.asList(f.getTag()), f.getPrecoPorLitro()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return promocoesEncontradas;
    }
}
