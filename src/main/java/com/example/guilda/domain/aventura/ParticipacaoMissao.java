package com.example.guilda.domain.aventura;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "participacoes",
        schema = "aventura",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"missao_id", "aventureiro_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipacaoMissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Missão
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missao_id", nullable = false)
    private Missao missao;

    // 🔗 Aventureiro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aventureiro_id", nullable = false)
    private Aventureiro aventureiro;

    @Column(nullable = false)
    private String papel;

    private Integer recompensa; // >= 0

    @Column(nullable = false)
    private Boolean destaque;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}