package com.example.guilda.controller;
import com.example.guilda.domain.aventura.Missao;
import com.example.guilda.domain.aventura.NivelPerigo;
import com.example.guilda.domain.aventura.StatusMissao;
import com.example.guilda.dto.aventura.MissaoRelatorioDTO;
import com.example.guilda.service.MissaoService;
import com.example.guilda.repository.aventura.MissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/missoes")
@RequiredArgsConstructor
public class MissaoController {

    private final MissaoService service;
    private final MissaoRepository repository;

    // 🔹 Criar missão
    @PostMapping
    public Missao criar(@RequestBody Missao missao) {
        return service.criar(missao);
    }

    // 🔹 Listar com filtros
    @GetMapping
    public Page<Missao> listar(
            @RequestParam StatusMissao status,
            @RequestParam NivelPerigo nivel,
            @RequestParam OffsetDateTime inicio,
            @RequestParam OffsetDateTime fim,
            Pageable pageable
    ) {
        return repository.findByStatusAndNivelPerigoAndCreatedAtBetween(
                status, nivel, inicio, fim, pageable
        );
    }

    // 🔹 Buscar participantes da missão
    @GetMapping("/{id}/participantes")
    public List<?> participantes(@PathVariable Long id) {
        return repository.buscarParticipantes(id);
    }

    // 🔥 Relatório de missões
    @GetMapping("/relatorio")
    public List<MissaoRelatorioDTO> relatorio(
            @RequestParam OffsetDateTime inicio,
            @RequestParam OffsetDateTime fim
    ) {
        return repository.relatorio(inicio, fim);
    }
}