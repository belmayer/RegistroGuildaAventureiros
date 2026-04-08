package com.example.guilda.dto.aventura;

import java.util.List;


public class ErroResponse {

    private String mensagem;
    private List<String> detalhes;

    public ErroResponse(String mensagem, String detalhe) {
        this.mensagem = mensagem;
        this.detalhes = List.of(detalhe);
    }

    public String getMensagem() {
        return mensagem;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }
}