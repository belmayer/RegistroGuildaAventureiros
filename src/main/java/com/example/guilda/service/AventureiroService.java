package com.example.guilda.service;

import com.example.guilda.domain.aventura.Aventureiro;
import com.example.guilda.domain.aventura.ClasseAventureiro;
import com.example.guilda.repository.aventura.AventureiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        if (aventureiro.getNome() == null || aventureiro.getNome().isBlank()) {
            throw new RuntimeException("Nome obrigatório");
        }

        if (aventureiro.getClasse() == null) {
            throw new RuntimeException("Classe obrigatória");
        }

        if (aventureiro.getOrganizacao() == null) {
            throw new RuntimeException("Aventureiro precisa de organização");
        }

        if (aventureiro.getUsuario() == null) {
            throw new RuntimeException("Aventureiro precisa de usuário");
        }

        // 🔥 REGRA IMPORTANTE
        aventureiro.setAtivo(true);

        // timestamps
        aventureiro.setCreatedAt(OffsetDateTime.now());
        aventureiro.setUpdatedAt(OffsetDateTime.now());

        return repository.save(aventureiro);
    }

    public Aventureiro atualizar(Long id, Aventureiro dados) {

        Aventureiro existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aventureiro não encontrado"));

        existente.setNome(dados.getNome());
        existente.setClasse(dados.getClasse());
        existente.setNivel(dados.getNivel());

        existente.setUpdatedAt(OffsetDateTime.now());

        return repository.save(existente);
    }

    public Aventureiro buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Não encontrado"));
    }

    public void inativar(Long id) {
        Aventureiro a = buscarPorId(id);
        a.setAtivo(false);
        repository.save(a);
    }

    public void reativar(Long id) {
        Aventureiro a = buscarPorId(id);
        a.setAtivo(true);
        repository.save(a);
    }

    public Page<Aventureiro> listar(Boolean ativo, ClasseAventureiro classe, Integer nivelMin, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());


        return repository.findByAtivoAndClasseAndNivelGreaterThanEqual(
                ativo, classe, nivelMin == null ? 0 : nivelMin, pageable
        );
    }
}