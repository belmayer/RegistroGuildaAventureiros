package com.example.guilda.service;

import com.example.guilda.domain.aventura.ParticipacaoMissao;
import com.example.guilda.repository.aventura.ParticipacaoMissaoRepository;
import com.example.guilda.repository.aventura.AventureiroRepository;
import com.example.guilda.repository.aventura.MissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ParticipacaoMissaoService {

    private final ParticipacaoMissaoRepository repository;
    private final AventureiroRepository aventureiroRepository;
    private final MissaoRepository missaoRepository;

    public ParticipacaoMissao criar(ParticipacaoMissao participacao) {

        // 🔥 buscar entidades completas no banco
        var aventureiro = aventureiroRepository.findById(
                participacao.getAventureiro().getId()
        ).orElseThrow(() -> new RuntimeException("Aventureiro não encontrado"));

        var missao = missaoRepository.findById(
                participacao.getMissao().getId()
        ).orElseThrow(() -> new RuntimeException("Missão não encontrada"));

        // 🔥 substituir no objeto
        participacao.setAventureiro(aventureiro);
        participacao.setMissao(missao);

        // 🔥 não pode duplicar
        boolean exists = repository.existsByMissaoIdAndAventureiroId(
                missao.getId(),
                aventureiro.getId()
        );

        if (exists) {
            throw new RuntimeException("Aventureiro já está nessa missão");
        }

        // 🔥 ativo
        if (!Boolean.TRUE.equals(aventureiro.getAtivo())) {
            throw new RuntimeException("Aventureiro inativo não pode participar");
        }

        // 🔥 mesma organização
        if (!missao.getOrganizacao().getId()
                .equals(aventureiro.getOrganizacao().getId())) {

            throw new RuntimeException("Organizações diferentes");
        }

        // 🔥 (opcional mas recomendado) validar status da missão
        /*
        if (missao.getStatus() != StatusMissao.PLANEJADA) {
            throw new RuntimeException("Missão não aceita participantes");
        }
        */

        // 🔥 recompensa válida
        if (participacao.getRecompensa() != null && participacao.getRecompensa() < 0) {
            throw new RuntimeException("Recompensa inválida");
        }

        participacao.setCreatedAt(OffsetDateTime.now());

        return repository.save(participacao);
    }
}