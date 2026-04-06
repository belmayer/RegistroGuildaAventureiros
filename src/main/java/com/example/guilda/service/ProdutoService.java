package com.example.guilda.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.guilda.dto.aventura.ProdutoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ElasticsearchClient client;

    public List<ProdutoDTO> buscar(String termo) throws IOException {

        SearchResponse<ProdutoDTO> response = client.search(s -> s
                        .index("guilda_loja")
                        .query(q -> q
                                .multiMatch(m -> m
                                        .fields("nome", "descricao")
                                        .query(termo)
                                )
                        ),
                ProdutoDTO.class
        );

        return response.hits().hits().stream()
                .map(hit -> hit.source())
                .toList();
    }
}