package com.example.guilda.dto.aventura;

import com.example.guilda.domain.aventura.Especie;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanheiroRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Espécie é obrigatória")
    private Especie especie;

    @NotNull
    @Min(value = 0, message = "Lealdade mínima é 0")
    @Max(value = 100, message = "Lealdade máxima é 100")
    private Integer lealdade;
}
