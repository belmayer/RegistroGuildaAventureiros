package com.example.guilda.repository.aventura;

import com.example.guilda.domain.aventura.Missao;
import com.example.guilda.domain.aventura.ParticipacaoMissao;
import com.example.guilda.dto.aventura.RankingDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface ParticipacaoMissaoRepository extends JpaRepository<ParticipacaoMissao, Long> {

    // 🔹 EVITAR DUPLICIDADE
    boolean existsByMissaoIdAndAventureiroId(Long missaoId, Long aventureiroId);

    // 🔹 CONTAR PARTICIPAÇÕES
    @Query("""
        SELECT COUNT(p)
        FROM ParticipacaoMissao p
        WHERE p.aventureiro.id = :id
    """)
    Long contarParticipacoes(@Param("id") Long id);

    // 🔹 ÚLTIMA MISSÃO (usa Pageable pra pegar só 1)
    @Query("""
        SELECT p.missao
        FROM ParticipacaoMissao p
        WHERE p.aventureiro.id = :id
        ORDER BY p.createdAt DESC
    """)
    List<Missao> ultimaMissao(@Param("id") Long id, Pageable pageable);

    // 🔥 RANKING CORRIGIDO
    @Query("""
    SELECT new com.example.guilda.dto.aventura.RankingDTO(
        p.aventureiro.id,
        p.aventureiro.nome,
        COUNT(p),
        CAST(COALESCE(SUM(p.recompensa), 0) AS long),
        SUM(CASE WHEN p.destaque = true THEN 1L ELSE 0L END)
    )
    FROM ParticipacaoMissao p
    WHERE (:inicio IS NULL OR p.createdAt >= :inicio)
    AND (:fim IS NULL OR p.createdAt <= :fim)
    GROUP BY p.aventureiro.id, p.aventureiro.nome
    ORDER BY COUNT(p) DESC
""")
    List<RankingDTO> ranking(
            @Param("inicio") OffsetDateTime inicio,
            @Param("fim") OffsetDateTime fim
    );
}