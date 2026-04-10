package com.example.guilda.service;

import com.example.guilda.domain.aventura.*;
import com.example.guilda.dto.aventura.RankingDTO;
import com.example.guilda.repository.aventura.ParticipacaoMissaoRepository;
import com.example.guilda.repository.aventura.AventureiroRepository;
import com.example.guilda.repository.aventura.MissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipacaoMissaoService {

    private final ParticipacaoMissaoRepository repository;
    private final AventureiroRepository aventureiroRepository;
    private final MissaoRepository missaoRepository;

    @Transactional
    public ParticipacaoMissao criar(ParticipacaoMissao participacao) {

        // 🔴 valida entrada básica
        if (participacao.getAventureiro() == null || participacao.getAventureiro().getId() == null) {
            throw new IllegalArgumentException("Aventureiro é obrigatório");
        }

        if (participacao.getMissao() == null || participacao.getMissao().getId() == null) {
            throw new IllegalArgumentException("Missão é obrigatória");
        }

        // 🔴 buscar entidades reais
        var aventureiro = aventureiroRepository.findById(
                participacao.getAventureiro().getId()
        ).orElseThrow(() -> new IllegalArgumentException("Aventureiro não encontrado"));

        var missao = missaoRepository.findById(
                participacao.getMissao().getId()
        ).orElseThrow(() -> new IllegalArgumentException("Missão não encontrada"));

        participacao.setAventureiro(aventureiro);
        participacao.setMissao(missao);

        // 🔥 CORREÇÃO CRÍTICA: setar ID composto
        participacao.setId(
                new ParticipacaoMissaoID(
                        missao.getId(),
                        aventureiro.getId()
                )
        );

        // 🔴 não duplicar participação
        if (repository.existsByMissaoIdAndAventureiroId(
                missao.getId(), aventureiro.getId())) {

            throw new IllegalArgumentException("Aventureiro já está nessa missão");
        }

        // 🔴 aventureiro ativo
        if (!Boolean.TRUE.equals(aventureiro.getAtivo())) {
            throw new IllegalArgumentException("Aventureiro inativo não pode participar");
        }

        // 🔴 mesma organização
        if (!missao.getOrganizacao().getId()
                .equals(aventureiro.getOrganizacao().getId())) {

            throw new IllegalArgumentException("Aventureiro e missão devem ser da mesma organização");
        }

        // 🔴 status da missão
        if (missao.getStatus() == StatusMissao.CANCELADA ||
            missao.getStatus() == StatusMissao.CONCLUIDA) {

            throw new IllegalArgumentException("Missão não aceita participantes");
        }

        // 🔴 papel obrigatório
        if (participacao.getPapel() == null || participacao.getPapel().isBlank()) {
            throw new IllegalArgumentException("Papel é obrigatório");
        }

        // 🔴 destaque obrigatório
        if (participacao.getDestaque() == null) {
            throw new IllegalArgumentException("Destaque deve ser informado");
        }

        // 🔴 recompensa válida
        if (participacao.getRecompensa() != null &&
            participacao.getRecompensa().compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException("Recompensa não pode ser negativa");
        }

        // 🔥 agora você controla o timestamp aqui
        participacao.setCreatedAt(OffsetDateTime.now());

        return repository.save(participacao);
    }

    public List<ParticipacaoMissao> buscarPorMissao(Long missaoId) {
        return repository.buscarPorMissao(missaoId);
    }

    public List<RankingDTO> ranking(
            OffsetDateTime inicio,
            OffsetDateTime fim,
            StatusMissao status
    ) {
        return repository.ranking(inicio, fim, status);
    }
}