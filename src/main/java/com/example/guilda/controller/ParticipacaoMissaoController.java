package com.example.guilda.controller;

import com.example.guilda.domain.aventura.ParticipacaoMissao;
import com.example.guilda.domain.aventura.StatusMissao;
import com.example.guilda.dto.aventura.ParticipacaoResponse;
import com.example.guilda.dto.aventura.RankingDTO;
import com.example.guilda.service.ParticipacaoMissaoService;
import com.example.guilda.repository.aventura.ParticipacaoMissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/participacoes")
@RequiredArgsConstructor
public class ParticipacaoMissaoController {

    private final ParticipacaoMissaoService service;

    // criar
    @PostMapping
    public ParticipacaoResponse criar(@RequestBody ParticipacaoMissao participacao) {
        return new ParticipacaoResponse(service.criar(participacao));
    }

    // llistar participantes de missao
    @GetMapping("/missao/{missaoId}")
    public List<ParticipacaoResponse> participantes(@PathVariable Long missaoId) {
        return service.buscarPorMissao(missaoId)
                .stream()
                .map(ParticipacaoResponse::new)
                .toList();
    }

    @GetMapping("/ranking")
    public List<RankingDTO> ranking(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            OffsetDateTime inicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            OffsetDateTime fim,

            @RequestParam(required = false)
            StatusMissao status
    ) {

        if (inicio != null && fim != null && inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data início não pode ser maior que data fim");
        }

        return service.ranking(inicio, fim, status);
    }
}