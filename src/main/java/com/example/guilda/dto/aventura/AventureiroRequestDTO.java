package com.example.guilda.dto.aventura;

import com.example.guilda.domain.aventura.ClasseAventureiro;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AventureiroRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Classe é obrigatória")
    private ClasseAventureiro classe;

    @NotNull(message = "Nível é obrigatório")
    @Min(value = 1, message = "Nível deve ser >= 1")
    private Integer nivel;
}