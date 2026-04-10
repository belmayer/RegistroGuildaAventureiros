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
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MissaoRepository extends JpaRepository<Missao, Long>,
        JpaSpecificationExecutor<Missao> {

    // 🔹 BUSCAR POR ID (já existe no JpaRepository, mas deixamos explícito)
    Optional<Missao> findById(Long id);

    // 🔥 RELATÓRIO DE MISSÕES (CORRIGIDO)
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
LEFT JOIN ParticipacaoMissao p
ON p.missao.id = m.id
AND p.createdAt BETWEEN :inicio AND :fim
GROUP BY m.id, m.titulo, m.status, m.nivelPerigo
""")
    List<MissaoRelatorioDTO> relatorio(
            @Param("inicio") OffsetDateTime inicio,
            @Param("fim") OffsetDateTime fim
    );
}