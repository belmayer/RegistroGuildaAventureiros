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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 ORGANIZAÇÃO (obrigatório)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    // 🔗 USUÁRIO (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id")
    private Usuario actorUser;

    // 🔗 API KEY (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_api_key_id")
    private ApiKey actorApiKey;

    // 📌 AÇÃO realizada (ex: CREATE_USER)
    @Column(nullable = false)
    private String action;

    // 📌 Schema da entidade afetada
    @Column(name = "entity_schema")
    private String entitySchema;

    // 📌 Nome da entidade (ex: usuarios)
    @Column(name = "entity_name")
    private String entityName;

    // 📌 ID da entidade afetada (string pq pode variar)
    @Column(name = "entity_id")
    private String entityId;

    // 📌 Momento da ação
    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    // 🌐 IP (INET no banco → String no Java)
    private String ip;

    // 🌐 Navegador / cliente
    @Column(name = "user_agent")
    private String userAgent;

    // 🔄 Diferenças (antes/depois)
    @Column(columnDefinition = "jsonb")
    private String diff;

    // 🧾 Dados extras
    @Column(columnDefinition = "jsonb")
    private String metadata;

    // ✅ Sucesso ou falha
    private Boolean success;
}