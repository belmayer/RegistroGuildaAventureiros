package com.example.guilda.operacoes;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaterialViewService {

    private final MaterialViewRepository repository;

    public MaterialViewService(MaterialViewRepository repository) {
        this.repository = repository;
    }

    @Cacheable("topMissoes")
    public List<MaterialView> buscarTopMissoes15Dias() {

        LocalDateTime dataLimite = LocalDateTime.now().minusDays(15);

        return repository
                .findTop10ByUltimaAtualizacaoAfterOrderByIndiceProntidaoDesc(dataLimite);
    }
}