package com.example.guilda.operacoes;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface MaterialViewRepository
        extends JpaRepository<MaterialView, Long> {

    List<MaterialView>
    findTop10ByUltimaAtualizacaoAfterOrderByIndiceProntidaoDesc(
            LocalDateTime dataLimite
    );
}