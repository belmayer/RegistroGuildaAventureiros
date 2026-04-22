package com.example.guilda.domain.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organizacao_seq")
    @SequenceGenerator(
            name = "organizacao_seq",
            sequenceName = "audit.organizacoes_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    // relacionamentos ================


    // 1:N com Usuario
    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Usuario> usuarios;

    // 1:N com Role
    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Role> roles;
}