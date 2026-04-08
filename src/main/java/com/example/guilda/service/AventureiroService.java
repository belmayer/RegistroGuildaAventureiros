package com.example.guilda.service;

import com.example.guilda.domain.aventura.Aventureiro;
import com.example.guilda.domain.aventura.ClasseAventureiro;
import com.example.guilda.dto.aventura.CompanheiroDTO;
import com.example.guilda.repository.aventura.AventureiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.guilda.dto.aventura.AventureiroDTO;
import org.springframework.web.server.ResponseStatusException;

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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aventureiro não encontrado"
                ));
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

    private AventureiroDTO toDTO(Aventureiro a) {

        CompanheiroDTO companheiroDTO = null;

        if (a.getCompanheiro() != null) {
            companheiroDTO = CompanheiroDTO.builder()
                    .nome(a.getCompanheiro().getNome())
                    .especie(a.getCompanheiro().getEspecie())
                    .lealdade(a.getCompanheiro().getLealdade())
                    .build();
        }

        return AventureiroDTO.builder()
                .id(a.getId())
                .nome(a.getNome())
                .classe(a.getClasse().name())
                .nivel(a.getNivel())
                .ativo(a.getAtivo())
                .organizacaoNome(a.getOrganizacao().getNome())
                .companheiro(companheiroDTO) // 🔥 AQUI
                .build();
    }

    public AventureiroDTO buscarDTO(Long id) {
        Aventureiro a = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aventureiro não encontrado"
                ));

        return toDTO(a);
    }

    public AventureiroDTO atualizarDTO(Long id, Aventureiro dados) {

        Aventureiro existente = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aventureiro não encontrado"
                ));

        existente.setNome(dados.getNome());
        existente.setClasse(dados.getClasse());
        existente.setNivel(dados.getNivel());
        existente.setUpdatedAt(OffsetDateTime.now());

        Aventureiro salvo = repository.save(existente);

        return toDTO(salvo);
    }

    public Page<AventureiroDTO> listar(Boolean ativo, ClasseAventureiro classe, Integer nivelMin, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));

        // nenhum filtro
        if (ativo == null && classe == null && nivelMin == null) {
            return repository.findAll(pageable)
                    .map(this::toDTO);
        }

        // só ativo
        if (ativo != null && classe == null && nivelMin == null) {
            return repository.findByAtivo(ativo, pageable)
                    .map(this::toDTO);
        }

        // só classe
        if (ativo == null && classe != null && nivelMin == null) {
            return repository.findByClasse(classe, pageable)
                    .map(this::toDTO);
        }

        // só nível
        if (ativo == null && classe == null && nivelMin != null) {
            return repository.findByNivelGreaterThanEqual(nivelMin, pageable)
                    .map(this::toDTO);
        }

        // ativo + classe
        if (ativo != null && classe != null && nivelMin == null) {
            return repository.findByAtivoAndClasse(ativo, classe, pageable)
                    .map(this::toDTO);
        }

        // ativo + nível
        if (ativo != null && classe == null && nivelMin != null) {
            return repository.findByAtivoAndNivelGreaterThanEqual(ativo, nivelMin, pageable)
                    .map(this::toDTO);
        }

        // classe + nível
        if (ativo == null && classe != null && nivelMin != null) {
            return repository.findByClasseAndNivelGreaterThanEqual(classe, nivelMin, pageable)
                    .map(this::toDTO);
        }

        // todos filtros
        return repository.findByAtivoAndClasseAndNivelGreaterThanEqual(
                ativo, classe, nivelMin, pageable)
                .map(this::toDTO);
    }

}