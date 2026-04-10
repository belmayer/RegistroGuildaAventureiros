package com.example.guilda.domain.aventura;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "participacao_missao", schema = "operacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipacaoMissao {

    @EmbeddedId
    private ParticipacaoMissaoID id;

    // missao
    @MapsId("missaoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "missao_id", nullable = false)
    private Missao missao;

    // aventureiro
    @MapsId("aventureiroId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aventureiro_id", nullable = false)
    private Aventureiro aventureiro;

    @Column(nullable = false)
    private String papel;

    @Column(name = "recompensa_ouro")
    private BigDecimal recompensa;

    @Column(nullable = false)
    private Boolean destaque;

    @Column(name = "data_registro", nullable = false, updatable = false)
    private OffsetDateTime createdAt;


}