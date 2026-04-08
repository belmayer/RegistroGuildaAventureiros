package com.example.guilda.domain.aventura;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "companheiro", schema = "operacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Companheiro {

    @Id
    @Column(name = "aventureiro_id")
    private Long id;

    // 🔗 1:1 com aventureiro (compartilha o ID)
    @OneToOne
    @MapsId
    @JoinColumn(name = "aventureiro_id")
    private Aventureiro aventureiro;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especie especie;

    @Column(name = "indice_lealdade", nullable = false)
    private Integer lealdade;
}