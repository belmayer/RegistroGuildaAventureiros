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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_seq")
    @SequenceGenerator(
            name = "permission_seq",
            sequenceName = "audit.permissions_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String descricao;

    // relacionamentos ================


    // role
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles;

}