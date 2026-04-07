package com.example.guilda.controller;

import com.example.guilda.domain.aventura.Aventureiro;
import com.example.guilda.domain.aventura.ClasseAventureiro;
import com.example.guilda.service.AventureiroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aventureiro")
@RequiredArgsConstructor
public class AventureiroController {

    private final AventureiroService service;

    @GetMapping("/{id}")
    public Aventureiro buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public Page<Aventureiro> listar(
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) ClasseAventureiro classe,
            @RequestParam(required = false) Integer nivelMin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.listar(ativo, classe, nivelMin, page, size);
    }

    @PatchMapping("/{id}/inativar")
    public void inativar(@PathVariable Long id) {
        service.inativar(id);
    }

    @PatchMapping("/{id}/reativar")
    public void reativar(@PathVariable Long id) {
        service.reativar(id);
    }

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
        return service.atualizar(id, aventureiro);
    }
}