package com.example.guilda.service;

import com.example.guilda.domain.aventura.*;
import com.example.guilda.dto.aventura.MissaoDetalhadaResponse;
import com.example.guilda.dto.aventura.MissaoRelatorioDTO;
import com.example.guilda.dto.aventura.MissaoRequestDTO;
import com.example.guilda.repository.audit.OrganizacaoRepository;
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
    private final OrganizacaoRepository organizacaoRepository;
    public MissaoDetalhadaResponse criar(MissaoRequestDTO dto) {

        Missao m = new Missao();

        m.setTitulo(dto.getTitulo());
        m.setNivelPerigo(dto.getNivelPerigo());
        m.setDataInicio(dto.getDataInicio());
        m.setDataFim(dto.getDataFim());

        if (dto.getDataInicio() != null && dto.getDataFim() != null &&
                dto.getDataFim().isBefore(dto.getDataInicio())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Data fim não pode ser antes da data início"
            );
        }

        if (dto.getTitulo().length() > 150) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Título deve ter no máximo 150 caracteres"
            );
        }

        m.setStatus(StatusMissao.PLANEJADA);
        m.setCreatedAt(OffsetDateTime.now());
        m.setOrganizacao(
                organizacaoRepository.findById(1L)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Organização não encontrada"
                        ))
        );

        Missao salva = repository.save(m);

        List<ParticipacaoMissao> participantes =
                participacaoRepository.buscarPorMissao(salva.getId());

        return new MissaoDetalhadaResponse(salva, participantes);
    }

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
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get("createdAt"), inicio));
            }

            if (fim != null) {
                predicates = cb.and(predicates, cb.lessThanOrEqualTo(root.get("createdAt"), fim));
            }

            return predicates;
        }, pageable);
    }

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