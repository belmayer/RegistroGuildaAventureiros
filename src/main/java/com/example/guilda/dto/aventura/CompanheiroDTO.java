package com.example.guilda.dto.aventura;

import com.example.guilda.domain.aventura.Especie;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanheiroDTO {

    private String nome;
    private Especie especie;
    private Integer lealdade;
}
