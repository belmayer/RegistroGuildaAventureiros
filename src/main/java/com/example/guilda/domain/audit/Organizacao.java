package com.example.guilda.domain.audit;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "organizacoes", schema = "audit")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    // relacionamentos ================


    // 🔹 1:N com Usuario
    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private Set<Usuario> usuarios;

    // 🔹 1:N com Role
    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private Set<Role> roles;
}