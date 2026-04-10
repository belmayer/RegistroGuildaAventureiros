package com.example.guilda.repository.aventura;

import com.example.guilda.domain.aventura.ParticipacaoMissao;
import com.example.guilda.domain.aventura.ParticipacaoMissaoID;
import com.example.guilda.domain.aventura.StatusMissao;
import com.example.guilda.dto.aventura.RankingDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface ParticipacaoMissaoRepository extends JpaRepository<ParticipacaoMissao, ParticipacaoMissaoID> {

    // 🔹 EVITAR DUPLICIDADE (forma mais segura)
    boolean existsByMissaoIdAndAventureiroId(Long missaoId, Long aventureiroId);

    // 🔥 PARTICIPANTES DA MISSÃO
    @Query("""
    SELECT p FROM ParticipacaoMissao p
    JOIN FETCH p.aventureiro
    WHERE p.missao.id = :missaoId
    """)
    List<ParticipacaoMissao> buscarPorMissao(@Param("missaoId") Long id);

    // 🔥 RANKING
    @Query("""
    SELECT new com.example.guilda.dto.aventura.RankingDTO(
        p.aventureiro.id,
        p.aventureiro.nome,
        COUNT(p),
        SUM(p.recompensa),
        SUM(CASE WHEN p.destaque = true THEN 1 ELSE 0 END)
    )
    FROM ParticipacaoMissao p
    WHERE (CAST(:inicio AS timestamp) IS NULL OR p.createdAt >= :inicio)
    AND (CAST(:fim AS timestamp) IS NULL OR p.createdAt <= :fim)
    AND (:status IS NULL OR p.missao.status = :status)
    GROUP BY p.aventureiro.id, p.aventureiro.nome
    ORDER BY COUNT(p) DESC
""")
    List<RankingDTO> ranking(
            @Param("inicio") OffsetDateTime inicio,
            @Param("fim") OffsetDateTime fim,
            @Param("status") StatusMissao status
    );
}