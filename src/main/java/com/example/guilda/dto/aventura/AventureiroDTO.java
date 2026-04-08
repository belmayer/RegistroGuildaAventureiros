package com.example.guilda.dto.aventura;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AventureiroDTO {

    private Long id;
    private String nome;
    private String classe;
    private Integer nivel;
    private Boolean ativo;
    private String organizacaoNome;
    private CompanheiroDTO companheiro;
}
