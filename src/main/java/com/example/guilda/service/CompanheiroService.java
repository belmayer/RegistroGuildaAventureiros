package com.example.guilda.service;

import com.example.guilda.domain.aventura.Companheiro;
import com.example.guilda.repository.aventura.CompanheiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanheiroService {

    private final CompanheiroRepository repository;

    public Companheiro criar(Companheiro companheiro) {

        if (companheiro.getAventureiro() == null) {
            throw new RuntimeException("Companheiro precisa de aventureiro");
        }

        if (companheiro.getLealdade() < 0 || companheiro.getLealdade() > 100) {
            throw new RuntimeException("Lealdade deve estar entre 0 e 100");
        }

        return repository.save(companheiro);
    }
}