package com.example.guilda.service;

import com.example.guilda.domain.aventura.Missao;
import com.example.guilda.repository.aventura.MissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class MissaoService {

    private final MissaoRepository repository;

    public Missao criar(Missao missao) {

        if (missao.getOrganizacao() == null) {
            throw new RuntimeException("Missão precisa de organização");
        }

        missao.setCreatedAt(OffsetDateTime.now());

        return repository.save(missao);
    }
}