package com.example.guilda.dto.aventura;

import com.example.guilda.domain.aventura.ParticipacaoMissao;
import lombok.Getter;

import java.math.BigDecimal;
@Getter
public class ParticipacaoResponse {

    private Long aventureiroId;
    private String nomeAventureiro;
    private String papel;
    private BigDecimal recompensa;
    private Boolean destaque;

    public ParticipacaoResponse(ParticipacaoMissao p) {
        this.aventureiroId = p.getAventureiro().getId();
        this.nomeAventureiro = p.getAventureiro().getNome();
        this.papel = p.getPapel();
        this.recompensa = p.getRecompensa();
        this.destaque = p.getDestaque();
    }
}
