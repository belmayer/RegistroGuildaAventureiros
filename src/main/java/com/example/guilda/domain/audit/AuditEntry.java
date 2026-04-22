package com.example.guilda.domain.audit;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_entries", schema = "audit")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_entry_seq")
    @SequenceGenerator(
            name = "audit_entry_seq",
            sequenceName = "audit.audit_entries_id_seq",
            allocationSize = 1
    )
    private Long id;

    // organizacao
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    // usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id")
    private Usuario actorUser;

    // apikey
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_api_key_id")
    private ApiKey actorApiKey;

    @Column(nullable = false)
    private String action;

    // schema da entidade
    @Column(name = "entity_schema")
    private String entitySchema;

    @Column(name = "entity_name")
    private String entityName;

    // ID da entidade
    @Column(name = "entity_id")
    private String entityId;

    // mometo da acao
    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    private String ip;

    @Column(name = "user_agent")
    private String userAgent;

    // antes/depois
    @Column(columnDefinition = "jsonb")
    private String diff;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    private Boolean success;
}