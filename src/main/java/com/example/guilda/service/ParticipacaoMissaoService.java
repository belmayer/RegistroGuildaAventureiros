package com.example.guilda.service;

import com.example.guilda.domain.aventura.ParticipacaoMissao;
import com.example.guilda.repository.aventura.ParticipacaoMissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ParticipacaoMissaoService {

    private final ParticipacaoMissaoRepository repository;

    public ParticipacaoMissao criar(ParticipacaoMissao participacao) {

        // 🔥 não pode duplicar
        boolean exists = repository.existsByMissaoIdAndAventureiroId(
                participacao.getMissao().getId(),
                participacao.getAventureiro().getId()
        );

        if (exists) {
            throw new RuntimeException("Aventureiro já está nessa missão");
        }

        // 🔥 ativo
        if (!participacao.getAventureiro().getAtivo()) {
            throw new RuntimeException("Aventureiro inativo não pode participar");
        }

        // 🔥 mesma organização
        if (!participacao.getMissao().getOrganizacao().getId()
                .equals(participacao.getAventureiro().getOrganizacao().getId())) {

            throw new RuntimeException("Organizações diferentes");
        }

        // 🔥 recompensa
        if (participacao.getRecompensa() != null && participacao.getRecompensa() < 0) {
            throw new RuntimeException("Recompensa inválida");
        }

        participacao.setCreatedAt(OffsetDateTime.now());

        return repository.save(participacao);
    }
}