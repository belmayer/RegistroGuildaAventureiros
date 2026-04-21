package com.example.guilda.service;

import com.example.guilda.domain.aventura.Aventureiro;
import com.example.guilda.domain.aventura.Companheiro;
import com.example.guilda.dto.aventura.CompanheiroDTO;
import com.example.guilda.dto.aventura.CompanheiroRequestDTO;
import com.example.guilda.repository.aventura.AventureiroRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanheiroService {

    private final AventureiroRepository aventureiroRepository;

    // criar ou substituir
    public CompanheiroDTO definirOuSubstituir(Long id, CompanheiroRequestDTO dto) {

        Aventureiro aventureiro = aventureiroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aventureiro não encontrado"
                ));

        Companheiro companheiro;

        // se já existe → atualiza
        if (aventureiro.getCompanheiro() != null) {
            companheiro = aventureiro.getCompanheiro();

        } else {
            // se não existe → cria novo
            companheiro = new Companheiro();
            companheiro.setAventureiro(aventureiro);
            aventureiro.setCompanheiro(companheiro);
        }

        // aplica dados do DTO
        companheiro.setNome(dto.getNome());
        companheiro.setEspecie(dto.getEspecie());
        companheiro.setLealdade(dto.getLealdade());

        aventureiroRepository.save(aventureiro);

        return toDTO(companheiro);
    }

    // remover companheiro
    public void remover(Long aventureiroId) {

        Aventureiro aventureiro = aventureiroRepository.findById(aventureiroId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aventureiro não encontrado"
                ));

        aventureiro.setCompanheiro(null);

        aventureiroRepository.save(aventureiro);
    }

    // conversão
    private CompanheiroDTO toDTO(Companheiro c) {
        return CompanheiroDTO.builder()
                .nome(c.getNome())
                .especie(c.getEspecie())
                .lealdade(c.getLealdade())
                .build();
    }
}