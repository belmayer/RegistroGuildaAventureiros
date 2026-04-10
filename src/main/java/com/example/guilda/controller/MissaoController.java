package com.example.guilda.controller;

import com.example.guilda.domain.aventura.Missao;
import com.example.guilda.domain.aventura.NivelPerigo;
import com.example.guilda.domain.aventura.StatusMissao;
import com.example.guilda.domain.aventura.ParticipacaoMissao;
import com.example.guilda.dto.aventura.MissaoDetalhadaResponse;
import com.example.guilda.dto.aventura.MissaoRelatorioDTO;
import com.example.guilda.service.MissaoService;
import com.example.guilda.repository.aventura.MissaoRepository;
import com.example.guilda.repository.aventura.ParticipacaoMissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/missao")
@RequiredArgsConstructor
public class MissaoController {

    private final MissaoService service;

    // criar missão
    @PostMapping
    public Missao criar(@RequestBody Missao missao) {
        return service.criar(missao);
    }

    //
    @GetMapping
    public Page<Missao> listar(
            @RequestParam(required = false) StatusMissao status,
            @RequestParam(required = false) NivelPerigo nivel,
            @RequestParam(required = false) OffsetDateTime inicio,
            @RequestParam(required = false) OffsetDateTime fim,
            Pageable pageable
    ) {
        return service.listar(status, nivel, inicio, fim, pageable);
    }

    @GetMapping("/{id}")
    public MissaoDetalhadaResponse detalhar(@PathVariable Long id) {
        return service.detalhar(id);
    }

    // 🔥 RELATÓRIO
    @GetMapping("/relatorio")
    public List<MissaoRelatorioDTO> relatorio(
            @RequestParam(required = false) OffsetDateTime inicio,
            @RequestParam(required = false) OffsetDateTime fim
    ) {
        return service.relatorio(inicio, fim);
    }
}