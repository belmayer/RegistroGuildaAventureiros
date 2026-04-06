package com.example.guilda.dto.aventura;


public record RankingDTO(
        Long aventureiroId,
        String nome,
        Long totalParticipacoes,
        Long totalRecompensa,
        Long totalDestaques
) {}