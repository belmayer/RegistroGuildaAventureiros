package com.example.guilda.repository.audit;

import com.example.guilda.domain.audit.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizacaoRepository extends JpaRepository<Organizacao, Long> {
}
