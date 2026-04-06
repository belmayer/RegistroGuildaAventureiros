package com.example.guilda.domain.audit;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(
        name = "permissions",
        schema = "audit",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"code"})
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String descricao;

    // relacionamentos ================


    // role
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles;

    // Role ---< role_permissions >--- Permission
}