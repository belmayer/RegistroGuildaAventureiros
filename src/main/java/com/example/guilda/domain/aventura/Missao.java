package com.example.guilda.domain.aventura;

import com.example.guilda.domain.audit.Organizacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "missao", schema = "operacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Missao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // organizacao
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    @JsonIgnore
    private Organizacao organizacao;

    // titulo
    @Column(nullable = false, length = 150)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelPerigo nivelPerigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMissao status;

    // data de criação automática
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "data_inicio")
    private OffsetDateTime dataInicio;

    @Column(name = "data_fim")
    private OffsetDateTime dataFim;

    // tiro ou não tiro??
    @PrePersist
    @PreUpdate
    public void antesDeSalvar() {

        // createdAt só na criação
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now();
        }

        // status padrão
        if (this.status == null) {
            this.status = StatusMissao.PLANEJADA;
        }

        // validação de datas
        if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
            throw new IllegalStateException("Data fim não pode ser antes da data início");
        }
    }
}