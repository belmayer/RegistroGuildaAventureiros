package com.example.guilda.controller;

import com.example.guilda.domain.aventura.Aventureiro;
import com.example.guilda.service.AventureiroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aventureiros")
@RequiredArgsConstructor
public class AventureiroController {

    private final AventureiroService service;

    // 🔹 Criar aventureiro
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Aventureiro criar(@RequestBody Aventureiro aventureiro) {
        return service.criar(aventureiro);
    }

    // 🔹 Atualizar aventureiro
    @PutMapping("/{id}")
    public Aventureiro atualizar(@PathVariable Long id,
                                 @RequestBody Aventureiro aventureiro) {

        aventureiro.setId(id);
        return service.atualizar(aventureiro);
    }
}