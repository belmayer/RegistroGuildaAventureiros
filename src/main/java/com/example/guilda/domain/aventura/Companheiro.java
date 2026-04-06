package com.example.guilda.domain.aventura;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "companheiros", schema = "aventura")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Companheiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 1:1 com aventureiro
    @OneToOne
    @JoinColumn(name = "aventureiro_id", nullable = false, unique = true)
    private Aventureiro aventureiro;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false)
    private String especie;

    @Column(nullable = false)
    private Integer lealdade; // 0 a 100
}