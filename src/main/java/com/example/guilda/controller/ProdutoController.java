package com.example.guilda.controller;

import com.example.guilda.dto.aventura.ProdutoDTO;
import com.example.guilda.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    // 🔎 já existia
    @GetMapping("/buscar")
    public List<ProdutoDTO> buscar(@RequestParam String termo) throws IOException {
        return service.buscar(termo);
    }

    // 🔎 1. nome
    @GetMapping("/busca/nome")
    public List<ProdutoDTO> nome(@RequestParam String termo) throws IOException {
        return service.buscarPorNome(termo);
    }

    // 🔎 2. descrição
    @GetMapping("/busca/descricao")
    public List<ProdutoDTO> descricao(@RequestParam String termo) throws IOException {
        return service.buscarPorDescricao(termo);
    }

    // 🔎 3. frase
    @GetMapping("/busca/frase")
    public List<ProdutoDTO> frase(@RequestParam String termo) throws IOException {
        return service.buscarFrase(termo);
    }

    // 🔎 4. fuzzy
    @GetMapping("/busca/fuzzy")
    public List<ProdutoDTO> fuzzy(@RequestParam String termo) throws IOException {
        return service.buscarFuzzy(termo);
    }

    // 🔎 5. multicampos
    @GetMapping("/busca/multicampos")
    public List<ProdutoDTO> multi(@RequestParam String termo) throws IOException {
        return service.buscarMultiCampos(termo);
    }

    @GetMapping("/busca/com-filtro")
    public List<ProdutoDTO> comFiltro(
            @RequestParam String termo,
            @RequestParam String categoria
    ) throws IOException {
        return service.buscarComFiltro(termo, categoria);
    }

    @GetMapping("/busca/faixa-preco")
    public List<ProdutoDTO> faixaPreco(
            @RequestParam double min,
            @RequestParam double max
    ) throws IOException {
        return service.buscarPorFaixaPreco(min, max);
    }

    @GetMapping("/busca/avancada")
    public List<ProdutoDTO> avancada(
            @RequestParam String categoria,
            @RequestParam String raridade,
            @RequestParam double min,
            @RequestParam double max
    ) throws IOException {
        return service.buscaAvancada(categoria, raridade, min, max);
    }

    @GetMapping("/agregacoes/por-categoria")
    public Map<String, Long> porCategoria() throws IOException {
        return service.quantidadePorCategoria();
    }

    @GetMapping("/agregacoes/por-raridade")
    public Map<String, Long> porRaridade() throws IOException {
        return service.quantidadePorRaridade();
    }

    @GetMapping("/agregacoes/preco-medio")
    public Double precoMedio() throws IOException {
        return service.precoMedio();
    }

    @GetMapping("/agregacoes/faixas-preco")
    public Map<String, Long> faixasPreco() throws IOException {
        return service.faixasPreco();
    }
}
