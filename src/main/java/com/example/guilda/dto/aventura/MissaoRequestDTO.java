package com.example.guilda.dto.aventura;

import com.example.guilda.domain.aventura.NivelPerigo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissaoRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotNull(message = "Nível de perigo é obrigatório")
    private NivelPerigo nivelPerigo;

    private OffsetDateTime dataInicio;
    private OffsetDateTime dataFim;
}