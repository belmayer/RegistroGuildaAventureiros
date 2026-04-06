package com.example.guilda.domain.audit;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(
        name = "roles",
        schema = "audit",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"organizacao_id", "nome"})
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(name = "created_at", nullable = false) // updatable = false ??
    private OffsetDateTime createdAt;

    // relacionamentos ================


    //usuarios
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Usuario> usuarios;

    //role
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
    name = "role_permissions",
    schema = "audit",
    joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    // organizacao
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    // Role ---< role_permissions >--- Permission
}