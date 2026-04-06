package com.example.guilda.controller;

import com.example.guilda.dto.aventura.ProdutoDTO;
import com.example.guilda.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @GetMapping("/buscar")
    public List<ProdutoDTO> buscar(@RequestParam String termo) throws IOException {
        return service.buscar(termo);
    }
}
