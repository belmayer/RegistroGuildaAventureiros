package com.example.guilda.repository.aventura;

import com.example.guilda.domain.aventura.Aventureiro;
import com.example.guilda.domain.aventura.ClasseAventureiro;
import com.example.guilda.domain.aventura.Missao;
import com.example.guilda.domain.aventura.ParticipacaoMissao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AventureiroRepository extends JpaRepository<Aventureiro, Long> {

    // filtros - status + classe + nível mínimo
    Page<Aventureiro> findByAtivoAndClasseAndNivelGreaterThanEqual(
            Boolean ativo,
            ClasseAventureiro classe,
            Integer nivel,
            Pageable pageable
    );

    // só ativo
    Page<Aventureiro> findByAtivo(Boolean ativo, Pageable pageable);

    // só classe
    Page<Aventureiro> findByClasse(ClasseAventureiro classe, Pageable pageable);

    // só nível mínimo
    Page<Aventureiro> findByNivelGreaterThanEqual(Integer nivel, Pageable pageable);

    // ativo + classe
    Page<Aventureiro> findByAtivoAndClasse(
            Boolean ativo,
            ClasseAventureiro classe,
            Pageable pageable
    );

    // ativo + nível
    Page<Aventureiro> findByAtivoAndNivelGreaterThanEqual(
            Boolean ativo,
            Integer nivel,
            Pageable pageable
    );

    // classe + nível
    Page<Aventureiro> findByClasseAndNivelGreaterThanEqual(
            ClasseAventureiro classe,
            Integer nivel,
            Pageable pageable
    );

    // busca nome
    Page<Aventureiro> findByNomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );

    // perfil completo
    @Query("""
        SELECT a FROM Aventureiro a
        LEFT JOIN FETCH a.organizacao
        LEFT JOIN FETCH a.usuario
        WHERE a.id = :id
    """)
    Aventureiro buscarCompleto(@Param("id") Long id);

}