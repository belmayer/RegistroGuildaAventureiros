package com.example.guilda.service;

import com.example.guilda.domain.aventura.Aventureiro;
import com.example.guilda.domain.aventura.Companheiro;
import com.example.guilda.domain.aventura.Especie;
import com.example.guilda.repository.aventura.AventureiroRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanheiroService {

    private final AventureiroRepository aventureiroRepository;

    // criar ou substituir
    public Companheiro definirOuSubstituir(Long id, Companheiro dados) {

        Aventureiro aventureiro = aventureiroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aventureiro não encontrado"));

        if (aventureiro.getCompanheiro() != null) {

            Companheiro existente = aventureiro.getCompanheiro();

            existente.setNome(dados.getNome());
            existente.setEspecie(dados.getEspecie());
            existente.setLealdade(dados.getLealdade());

            //  sincroniza
            existente.setAventureiro(aventureiro);

        } else {

            Companheiro novo = new Companheiro();

            novo.setNome(dados.getNome());
            novo.setEspecie(dados.getEspecie());
            novo.setLealdade(dados.getLealdade());

            novo.setAventureiro(aventureiro);

            aventureiro.setCompanheiro(novo);
        }

        aventureiroRepository.save(aventureiro);

        return aventureiro.getCompanheiro();
    }

    // apaga o companheiro
    public void remover(Long aventureiroId) {

        Aventureiro aventureiro = aventureiroRepository.findById(aventureiroId)
                .orElseThrow(() -> new RuntimeException("Aventureiro não encontrado"));

        aventureiro.setCompanheiro(null);

        aventureiroRepository.save(aventureiro);
    }

    // validações de regra
    private void validar(Companheiro c) {

        if (c.getNome() == null || c.getNome().isBlank()) {
            throw new RuntimeException("nome é obrigatório");
        }

        if (c.getEspecie() == null) {
            throw new RuntimeException("especie é obrigatória");
        }

        // garante enum válido
        try {
            Especie.valueOf(c.getEspecie().name());
        } catch (Exception e) {
            throw new RuntimeException("especie inválida");
        }

        if (c.getLealdade() == null || c.getLealdade() < 0 || c.getLealdade() > 100) {
            throw new RuntimeException("lealdade deve estar entre 0 e 100");
        }
    }
}