package com.example.guilda.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.example.guilda.dto.aventura.ProdutoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ElasticsearchClient client;

    private List<ProdutoDTO> extrair(SearchResponse<ProdutoDTO> response) {
        return response.hits().hits()
                .stream()
                .map(hit -> hit.source())
                .toList();
    }


    public List<ProdutoDTO> buscar(String termo) throws IOException {
        var response = client.search(s -> s
                        .index("guilda_loja")
                        .query(q -> q
                                .multiMatch(m -> m
                                        .fields("nome", "descricao")
                                        .query(termo)
                                )
                        ),
                ProdutoDTO.class
        );

        return extrair(response);
    }

    // 🔎 1. Busca por nome
    public List<ProdutoDTO> buscarPorNome(String termo) throws IOException {
        var response = client.search(s -> s
                        .index("guilda_loja")
                        .query(q -> q.match(m -> m
                                .field("nome")
                                .query(termo)
                        )),
                ProdutoDTO.class
        );

        return extrair(response);
    }

    // 🔎 2. Busca por descrição
    public List<ProdutoDTO> buscarPorDescricao(String termo) throws IOException {
        var response = client.search(s -> s
                        .index("guilda_loja")
                        .query(q -> q.match(m -> m
                                .field("descricao")
                                .query(termo)
                        )),
                ProdutoDTO.class
        );

        return extrair(response);
    }

    // 🔎 3. Frase exata
    public List<ProdutoDTO> buscarFrase(String termo) throws IOException {
        var response = client.search(s -> s
                        .index("guilda_loja")
                        .query(q -> q.matchPhrase(m -> m
                                .field("descricao")
                                .query(termo)
                        )),
                ProdutoDTO.class
        );

        return extrair(response);
    }

    // 🔎 4. Fuzzy
    public List<ProdutoDTO> buscarFuzzy(String termo) throws IOException {
        var response = client.search(s -> s
                        .index("guilda_loja")
                        .query(q -> q.match(m -> m
                                .field("nome")
                                .query(termo)
                                .fuzziness("AUTO")
                        )),
                ProdutoDTO.class
        );

        return extrair(response);
    }

    // 🔎 5. Multicampos (igual ao que você já tinha, mas separado)
    public List<ProdutoDTO> buscarMultiCampos(String termo) throws IOException {
        return buscar(termo);
    }

    // parte B

    public List<ProdutoDTO> buscarComFiltro(String termo, String categoria) throws IOException {

        var response = client.search(s -> s
                        .index("guilda_loja")
                        .query(q -> q.bool(b -> b
                                .must(m -> m.match(mt -> mt
                                        .field("descricao")
                                        .query(termo)
                                ))
                                .filter(f -> f.term(t -> t
                                        .field("categoria")
                                        .value(categoria)
                                ))
                        )),
                ProdutoDTO.class
        );

        return extrair(response);
    }

    public List<ProdutoDTO> buscarPorFaixaPreco(double min, double max) throws IOException {

        var response = client.search(s -> s
                        .index("guilda_loja")
                        .query(q -> q.range(r -> r
                                .field("preco")
                                .gte(JsonData.of(min))
                                .lte(JsonData.of(max))
                        )),
                ProdutoDTO.class
        );

        return extrair(response);
    }

    public List<ProdutoDTO> buscaAvancada(
            String categoria,
            String raridade,
            double min,
            double max
    ) throws IOException {

        var response = client.search(s -> s
                        .index("guilda_loja")
                        .query(q -> q.bool(b -> b
                                .filter(f -> f.term(t -> t
                                        .field("categoria")
                                        .value(categoria)
                                ))
                                .filter(f -> f.term(t -> t
                                        .field("raridade")
                                        .value(raridade)
                                ))
                                .filter(f -> f.range(r -> r
                                        .field("preco")
                                        .gte(JsonData.of(min))
                                        .lte(JsonData.of(max))
                                ))
                        )),
                ProdutoDTO.class
        );

        return extrair(response);
    }


    // parte c


    public Map<String, Long> quantidadePorCategoria() throws IOException {

        var response = client.search(s -> s
                        .index("guilda_loja")
                        .size(0)
                        .aggregations("categorias", a -> a
                                .terms(t -> t.field("categoria.keyword"))
                        ),
                Void.class
        );

        Map<String, Long> resultado = new HashMap<>();

        var buckets = response.aggregations()
                .get("categorias")
                .sterms()
                .buckets()
                .array();

        for (var bucket : buckets) {
            resultado.put(bucket.key().stringValue(), bucket.docCount());
        }

        return resultado;
    }

    public Map<String, Long> quantidadePorRaridade() throws IOException {

        var response = client.search(s -> s
                        .index("guilda_loja")
                        .size(0)
                        .aggregations("raridades", a -> a
                                .terms(t -> t.field("raridade.keyword"))
                        ),
                Void.class
        );

        Map<String, Long> resultado = new HashMap<>();

        var buckets = response.aggregations()
                .get("raridades")
                .sterms()
                .buckets()
                .array();

        for (var bucket : buckets) {
            resultado.put(bucket.key().stringValue(), bucket.docCount());
        }

        return resultado;
    }

    public Double precoMedio() throws IOException {

        var response = client.search(s -> s
                        .index("guilda_loja")
                        .size(0)
                        .aggregations("media_preco", a -> a
                                .avg(avg -> avg.field("preco"))
                        ),
                Void.class
        );

        return response.aggregations()
                .get("media_preco")
                .avg()
                .value();
    }

    public Map<String, Long> faixasPreco() throws IOException {

        Map<String, Long> resultado = new HashMap<>();

        resultado.put("abaixo_100", contar(null, 100.0));
        resultado.put("100_300", contar(100.0, 300.0));
        resultado.put("300_700", contar(300.0, 700.0));
        resultado.put("acima_700", contar(700.0, null));

        return resultado;
    }

    private long contar(Double min, Double max) throws IOException {

        var response = client.count(c -> c
                .index("guilda_loja")
                .query(q -> q
                        .range(r -> {
                            r.field("preco");
                            if (min != null) r.gte(JsonData.of(min));
                            if (max != null) r.lt(JsonData.of(max));
                            return r;
                        })
                )
        );

        return response.count();
    }
}