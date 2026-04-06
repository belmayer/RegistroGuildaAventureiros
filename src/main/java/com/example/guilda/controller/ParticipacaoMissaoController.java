package com.example.guilda.controller;

import com.example.guilda.domain.aventura.Missao;
import com.example.guilda.domain.aventura.ParticipacaoMissao;
import com.example.guilda.dto.aventura.RankingDTO;
import com.example.guilda.service.ParticipacaoMissaoService;
import com.example.guilda.repository.aventura.ParticipacaoMissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/participacoes")
@RequiredArgsConstructor
public class ParticipacaoMissaoController {

    private final ParticipacaoMissaoService service;
    private final ParticipacaoMissaoRepository repository;

    // 🔹 Criar participação
    @PostMapping
    public ParticipacaoMissao criar(@RequestBody ParticipacaoMissao participacao) {
        return service.criar(participacao);
    }

    // 🔹 Contar participações de um aventureiro
    @GetMapping("/aventureiro/{id}/total")
    public Long totalParticipacoes(@PathVariable Long id) {
        return repository.contarParticipacoes(id);
    }

    // 🔹 Última missão do aventureiro
    @GetMapping("/aventureiro/{id}/ultima-missao")
    public Missao ultimaMissao(@PathVariable Long id) {
        return repository.ultimaMissao(id, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);
    }

    // 🔥 Ranking
    @GetMapping("/ranking")
    public List<RankingDTO> ranking(
            @RequestParam(required = false) OffsetDateTime inicio,
            @RequestParam(required = false) OffsetDateTime fim
    ) {
        return repository.ranking(inicio, fim);
    }
}