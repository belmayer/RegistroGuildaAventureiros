package com.example.guilda.service;

import com.example.guilda.domain.aventura.Missao;
import com.example.guilda.domain.aventura.NivelPerigo;
import com.example.guilda.domain.aventura.ParticipacaoMissao;
import com.example.guilda.domain.aventura.StatusMissao;
import com.example.guilda.dto.aventura.MissaoDetalhadaResponse;
import com.example.guilda.dto.aventura.MissaoRelatorioDTO;
import com.example.guilda.repository.aventura.MissaoRepository;
import com.example.guilda.repository.aventura.ParticipacaoMissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissaoService {

    private final MissaoRepository repository;
    private final ParticipacaoMissaoRepository participacaoRepository;

    public Missao criar(Missao missao) {

        if (missao.getOrganizacao() == null) {
            throw new IllegalArgumentException("Missão precisa de organização");
        }

        if (missao.getTitulo() == null || missao.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }

        if (missao.getTitulo().length() > 150) {
            throw new IllegalArgumentException("Título deve ter no máximo 150 caracteres");
        }

        if (missao.getNivelPerigo() == null) {
            throw new IllegalArgumentException("Nível de perigo é obrigatório");
        }

        if (missao.getStatus() == null) {
            missao.setStatus(StatusMissao.PLANEJADA);
        }

        if (missao.getDataInicio() != null && missao.getDataFim() != null &&
                missao.getDataFim().isBefore(missao.getDataInicio())) {
            throw new IllegalArgumentException("Data fim não pode ser antes da data início");
        }

        return repository.save(missao);
    }

    // 🔥 LISTAGEM DINÂMICA CORRETA
    public Page<Missao> listar(
            StatusMissao status,
            NivelPerigo nivel,
            OffsetDateTime inicio,
            OffsetDateTime fim,
            Pageable pageable
    ) {

        return repository.findAll((root, query, cb) -> {

            var predicates = cb.conjunction();

            if (status != null) {
                predicates = cb.and(predicates, cb.equal(root.get("status"), status));
            }

            if (nivel != null) {
                predicates = cb.and(predicates, cb.equal(root.get("nivelPerigo"), nivel));
            }

            if (inicio != null) {
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get("data_criacao"), inicio));
            }

            if (fim != null) {
                predicates = cb.and(predicates, cb.lessThanOrEqualTo(root.get("data_criacao"), fim));
            }

            return predicates;
        }, pageable);
    }

    // 🔥 NOVO: DETALHAR (AGORA CORRETO)
    public MissaoDetalhadaResponse detalhar(Long id) {

        Missao missao = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Missão não encontrada"));

        List<ParticipacaoMissao> participantes =
                participacaoRepository.buscarPorMissao(id);

        return new MissaoDetalhadaResponse(missao, participantes);
    }

    public List<MissaoRelatorioDTO> relatorio(
            OffsetDateTime inicio,
            OffsetDateTime fim
    ) {

        if (inicio == null) {
            inicio = OffsetDateTime.parse("1900-01-01T00:00:00Z");
        }

        if (fim == null) {
            fim = OffsetDateTime.now();
        }

        return repository.relatorio(inicio, fim);
    }
}