package br.com.cervejamatch.main.promocoes.controller;

import br.com.cervejamatch.main.promocoes.controller.converter.PromocaoConverter;
import br.com.cervejamatch.main.promocoes.model.Promocao;
import br.com.cervejamatch.main.promocoes.repository.PromocaoRepository;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.AdditionalAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PromocoesController.class)
public class PromocoesControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PromocaoRepository promocaoRepository;

    @TestConfiguration
    static class AdditionalConfig {
        @Bean
        public PromocaoConverter promocaoConverter() {
            return Mappers.getMapper(PromocaoConverter.class);
        }
    }

    @Test
    public void shouldListPromocoes() throws Exception {
        when(promocaoRepository.findAll()).thenReturn(Arrays.asList(
           new Promocao("id1", "Eisenbahn", "endereco1", "loja1", "autor1", 10.0, new GeoJsonPoint(0.0, 1.0), Arrays.asList("tag1", "tag2")),
           new Promocao("id2", "brahma", "endereco2", "loja2", "autor2", 10.0, new GeoJsonPoint(0.0, 2.0), Collections.emptyList())
        ));

        mockMvc.perform(get("/promocoes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", equalTo("id1")))
                .andExpect(jsonPath("$.[0].descricao", equalTo("Eisenbahn")))
                .andExpect(jsonPath("$.[0].endereco", equalTo("endereco1")))
                .andExpect(jsonPath("$.[0].loja", equalTo("loja1")))
                .andExpect(jsonPath("$.[0].precoPorLitro", equalTo(10.0)))
                .andExpect(jsonPath("$.[0].criadoPor", equalTo("autor1")))
                .andExpect(jsonPath("$.[0].localizacao.x", equalTo(0.0)))
                .andExpect(jsonPath("$.[0].localizacao.y", equalTo(1.0)))
                .andExpect(jsonPath("$.[0].tags", hasItems("tag1", "tag2")))
        ;
    }

    @Test
    public void shouldCreatePromocao() throws Exception {
        when(promocaoRepository.save(any())).thenAnswer(invocationOnMock -> {
            Promocao argument = invocationOnMock.getArgument(0);
            return new Promocao("id", argument.getDescricao(), argument.getEndereco(), argument.getLoja(), argument.getCriadoPor(), argument.getPrecoPorLitro(), argument.getLocalizacao(), argument.getTags());
        });

        String content =
                "{\n" +
                "    \"descricao\": \"Skol\",\n" +
                "    \"endereco\": \"endereco\",\n" +
                "    \"loja\": \"loja\",\n" +
                "    \"criadoPor\": \"autor\",\n" +
                "    \"precoPorLitro\": \"10.0\",\n" +
                "    \"localizacao\": {\n" +
                "        \"coordinates\": [0.0,1.0]\n" +
                "    },\n" +
                "    \"tags\": [\"tag1\"]\n" +
                "}";

        mockMvc.perform(post("/promocoes").contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo("id")))
                .andExpect(jsonPath("$.descricao", equalTo("Skol")))
                .andExpect(jsonPath("$.endereco", equalTo("endereco")))
                .andExpect(jsonPath("$.loja", equalTo("loja")))
                .andExpect(jsonPath("$.criadoPor", equalTo("autor")))
                .andExpect(jsonPath("$.precoPorLitro", equalTo(10.0)))
                .andExpect(jsonPath("$.localizacao.x", equalTo(0.0)))
                .andExpect(jsonPath("$.localizacao.y", equalTo(1.0)))
                .andExpect(jsonPath("$.tags", hasItems("tag1")))
                ;

        verify(promocaoRepository).save(any());
    }
}