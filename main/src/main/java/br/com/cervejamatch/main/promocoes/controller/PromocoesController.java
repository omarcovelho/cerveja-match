package br.com.cervejamatch.main.promocoes.controller;

import br.com.cervejamatch.main.promocoes.controller.converter.PromocaoConverter;
import br.com.cervejamatch.main.promocoes.controller.dto.PromocaoDTO;
import br.com.cervejamatch.main.promocoes.repository.PromocaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promocoes")
@RequiredArgsConstructor
public class PromocoesController {

    private final PromocaoRepository promocaoRepository;
    private final PromocaoConverter promocaoConverter;

    @GetMapping
    public ResponseEntity<?> getPromocoes() {
        return ResponseEntity.ok(promocaoRepository.findAll().stream().map(promocaoConverter::toDto));
    }

    @PostMapping
    public ResponseEntity<?> createPromocao(@RequestBody PromocaoDTO promocaoDTO)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(promocaoConverter.toDto(promocaoRepository.save(promocaoConverter.toModel(promocaoDTO))));
    }
}
