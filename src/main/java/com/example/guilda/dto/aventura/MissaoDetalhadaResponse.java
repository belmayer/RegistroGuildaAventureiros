package com.example.guilda.dto.aventura;

import com.example.guilda.domain.aventura.Missao;
import com.example.guilda.domain.aventura.ParticipacaoMissao;
import lombok.Getter;

import java.util.List;
@Getter
public class MissaoDetalhadaResponse {

    private Long id;
    private String titulo;
    private List<ParticipacaoResponse> participantes;

    public MissaoDetalhadaResponse(Missao missao, List<ParticipacaoMissao> participacoes) {
        this.id = missao.getId();
        this.titulo = missao.getTitulo();

        this.participantes = participacoes.stream()
                .map(ParticipacaoResponse::new)
                .toList();
    }
}