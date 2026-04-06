package com.example.guilda.service;

import com.example.guilda.domain.aventura.Aventureiro;
import com.example.guilda.repository.aventura.AventureiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AventureiroService {

    private final AventureiroRepository repository;

    public Aventureiro criar(Aventureiro aventureiro) {

        if (aventureiro.getNivel() < 1) {
            throw new RuntimeException("Nível deve ser >= 1");
        }

        if (aventureiro.getOrganizacao() == null) {
            throw new RuntimeException("Aventureiro precisa de organização");
        }

        if (aventureiro.getUsuario() == null) {
            throw new RuntimeException("Aventureiro precisa de usuário");
        }

        aventureiro.setCreatedAt(OffsetDateTime.now());
        aventureiro.setUpdatedAt(OffsetDateTime.now());

        return repository.save(aventureiro);
    }

    public Aventureiro atualizar(Aventureiro aventureiro) {
        aventureiro.setUpdatedAt(OffsetDateTime.now());
        return repository.save(aventureiro);
    }
}