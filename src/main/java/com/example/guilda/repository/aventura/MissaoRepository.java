package com.example.guilda.repository.aventura;

import com.example.guilda.domain.aventura.Missao;
import com.example.guilda.domain.aventura.NivelPerigo;
import com.example.guilda.domain.aventura.StatusMissao;
import com.example.guilda.dto.aventura.MissaoRelatorioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface MissaoRepository extends JpaRepository<Missao, Long> {

    // 🔹 LISTAGEM COM FILTROS
    Page<Missao> findByStatusAndNivelPerigoAndCreatedAtBetween(
            StatusMissao status,
            NivelPerigo nivel,
            OffsetDateTime inicio,
            OffsetDateTime fim,
            Pageable pageable
    );

    // 🔹 DETALHAMENTO (participantes)
    @Query("""
        SELECT p FROM ParticipacaoMissao p
        JOIN FETCH p.aventureiro
        WHERE p.missao.id = :missaoId
    """)
    List<?> buscarParticipantes(@Param("missaoId") Long missaoId);

    // 🔥 RELATÓRIO DE MISSÕES
    @Query("""
    SELECT new com.example.guilda.dto.aventura.MissaoRelatorioDTO(
        m.id,
        m.titulo,
        m.status,
        m.nivelPerigo,
        COUNT(p),
        SUM(p.recompensa)
    )
    FROM Missao m
    LEFT JOIN ParticipacaoMissao p ON p.missao.id = m.id
    WHERE m.createdAt BETWEEN :inicio AND :fim
    GROUP BY m.id, m.titulo, m.status, m.nivelPerigo
""")
    List<MissaoRelatorioDTO> relatorio(
            @Param("inicio") OffsetDateTime inicio,
            @Param("fim") OffsetDateTime fim
    );
}