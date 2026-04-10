package com.example.guilda.dto.aventura;

import java.math.BigDecimal;

public record RankingDTO(
        Long aventureiroId,
        String nome,
        Long totalParticipacoes,
        BigDecimal totalRecompensa,
        Long totalDestaques
) {
    public BigDecimal totalRecompensa() {
        return totalRecompensa == null ? BigDecimal.ZERO : totalRecompensa;
    }
}