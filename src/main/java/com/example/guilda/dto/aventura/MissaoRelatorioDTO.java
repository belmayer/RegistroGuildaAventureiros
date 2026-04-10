package com.example.guilda.dto.aventura;

import com.example.guilda.domain.aventura.StatusMissao;
import com.example.guilda.domain.aventura.NivelPerigo;

import java.math.BigDecimal;

public record MissaoRelatorioDTO(
        Long id,
        String titulo,
        StatusMissao status,
        NivelPerigo nivelPerigo,
        Long totalParticipantes,
        BigDecimal totalRecompensa
) {}