package br.com.cervejamatch.main.users.controller;

import br.com.cervejamatch.main.users.controller.converter.UserConverter;
import br.com.cervejamatch.main.users.model.DefinicoesDeFiltro;
import br.com.cervejamatch.main.users.model.FiltroDePromocao;
import br.com.cervejamatch.main.users.model.User;
import br.com.cervejamatch.main.users.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)
public class UsersControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserRepository userRepository;

    @TestConfiguration
    static class AdditionalConfig {
        @Bean
        public UserConverter userConverter() {
            return Mappers.getMapper(UserConverter.class);
        }
    }

    @Test
    public void shouldListUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(Arrays.asList(
            new User("id1", "nome1", "sobrenome1", "email1@teste.com", new DefinicoesDeFiltro("endereco1", new GeoJsonPoint(0.0, 1.0), Arrays.asList(new FiltroDePromocao("cerveja1", "IPA", 15.0)), 10.0)),
            new User("id2", "nome2", "sobrenome2", "email2@teste.com", new DefinicoesDeFiltro("endereco2", new GeoJsonPoint(0.0, 1.0), Arrays.asList(new FiltroDePromocao("cerveja2", "IPA", 15.0)), 10.0)),
            new User("id3", "nome3", "sobrenome3", "email3@teste.com", new DefinicoesDeFiltro("endereco2", new GeoJsonPoint(0.0, 1.0), Arrays.asList(new FiltroDePromocao("cerveja2", "IPA", 15.0)), 10.0))
        ));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].id", equalTo("id1")))
                .andExpect(jsonPath("$.[0].nome", equalTo("nome1")))
                .andExpect(jsonPath("$.[0].sobrenome", equalTo("sobrenome1")))
                .andExpect(jsonPath("$.[0].email", equalTo("email1@teste.com")))
                .andExpect(jsonPath("$.[0].definicoesDeFiltro.endereco", equalTo("endereco1")))
                .andExpect(jsonPath("$.[0].definicoesDeFiltro.localizacao.x", equalTo(0.0)))
                .andExpect(jsonPath("$.[0].definicoesDeFiltro.localizacao.y", equalTo(1.0)))
                .andExpect(jsonPath("$.[0].definicoesDeFiltro.filtros", hasSize(1)))
                .andExpect(jsonPath("$.[0].definicoesDeFiltro.filtros.[0].nome", equalTo("cerveja1")))
                .andExpect(jsonPath("$.[0].definicoesDeFiltro.filtros.[0].tag", equalTo("IPA")))
                .andExpect(jsonPath("$.[0].definicoesDeFiltro.filtros.[0].precoPorLitro", equalTo(15.0)))
        ;
    }

    @Test
    public void shouldCreateUser() throws Exception {
        when(userRepository.save(Mockito.any(User.class))).then(invocationOnMock -> {
            User argument = invocationOnMock.getArgument(0);
            return new User("id", argument.getNome(), argument.getSobrenome(), argument.getEmail(), null);
        });

        String content =
                "{" +
                "    \"nome\": \"nome1\",\n" +
                "    \"sobrenome\": \"sobrenome1\",\n" +
                "    \"email\":  \"teste@teste.com\"\n" +
                "}";

        mockMvc.perform(post("/users").content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo("id")))
                .andExpect(jsonPath("$.nome", equalTo("nome1")))
                .andExpect(jsonPath("$.sobrenome", equalTo("sobrenome1")))
                .andExpect(jsonPath("$.email", equalTo("teste@teste.com")))
        ;

        verify(userRepository).save(Mockito.any());
    }

    @Test
    public void shouldReturnBadRequestForExistingUser() throws Exception {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User("id", "nome", "sobrenome", "email", null)));

        String content =
                "{" +
                "    \"nome\": \"nome1\",\n" +
                "    \"sobrenome\": \"sobrenome1\",\n" +
                "    \"email\":  \"teste@teste.com\"\n" +
                "}";

        mockMvc.perform(post("/users").content(content).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

        verify(userRepository).findByEmail(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        when(userRepository.findById(eq("id"))).thenReturn(Optional.of(new User("id", "nome1", "sobrenome1", "teste@teste.com", null)));
        when(userRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        String content =
                "{" +
                "    \"nome\": \"nome1alterado\",\n" +
                "    \"sobrenome\": \"sobrenome1alterado\",\n" +
                "    \"email\":  \"alterado@teste.com\"\n" +
                "}";

        mockMvc.perform(put("/users/id").content(content).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo("id")))
                .andExpect(jsonPath("$.nome", equalTo("nome1alterado")))
                .andExpect(jsonPath("$.sobrenome", equalTo("sobrenome1alterado")))
                .andExpect(jsonPath("$.email", equalTo("teste@teste.com")))
        ;

        verify(userRepository).findById(anyString());
        verify(userRepository).save(any());
    }

    @Test
    public void shouldReturn404() throws Exception {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        String content =
                "{" +
                "    \"nome\": \"nome1alterado\",\n" +
                "    \"sobrenome\": \"sobrenome1alterado\",\n" +
                "    \"email\":  \"alterado@teste.com\"\n" +
                "}";

        mockMvc.perform(put("/users/id").content(content).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;

        verify(userRepository).findById(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldUpdateFiltrosDoUsuario() throws Exception {
        when(userRepository.findById(eq("id")))
                .thenReturn(Optional.of(new User("id", "nome1", "sobrenome1", "teste@teste.com", null)));
        when(userRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        String content = "{" +
                "    \"endereco\": \"endereco1\"," +
                "    \"localizacao\": {" +
                "        \"coordinates\": [0.0, 1.0]" +
                "    }," +
                "    \"filtros\": [{" +
                "        \"nome\": \"cerveja1\"," +
                "        \"tag\": \"IPA\"," +
                "        \"precoPorLitro\": 15.0" +
                "    }]" +
                "}";

        mockMvc.perform(put("/users/id/filtros").contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(jsonPath("$.id", equalTo("id")))
                .andExpect(jsonPath("$.nome", equalTo("nome1")))
                .andExpect(jsonPath("$.sobrenome", equalTo("sobrenome1")))
                .andExpect(jsonPath("$.email", equalTo("teste@teste.com")))
                .andExpect(jsonPath("$.definicoesDeFiltro.endereco", equalTo("endereco1")))
                .andExpect(jsonPath("$.definicoesDeFiltro.localizacao.x", equalTo(0.0)))
                .andExpect(jsonPath("$.definicoesDeFiltro.localizacao.y", equalTo(1.0)))
                .andExpect(jsonPath("$.definicoesDeFiltro.filtros", hasSize(1)))
                .andExpect(jsonPath("$.definicoesDeFiltro.filtros.[0].nome", equalTo("cerveja1")))
                .andExpect(jsonPath("$.definicoesDeFiltro.filtros.[0].tag", equalTo("IPA")))
                .andExpect(jsonPath("$.definicoesDeFiltro.filtros.[0].precoPorLitro", equalTo(15.0)))
        ;

        verify(userRepository).findById(anyString());
        verify(userRepository).save(any());
    }
}