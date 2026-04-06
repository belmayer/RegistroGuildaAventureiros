package com.example.guilda.dto.aventura;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {

    private String nome;
    private String descricao;
    private String categoria;
    private String raridade;
    private Double preco;
}