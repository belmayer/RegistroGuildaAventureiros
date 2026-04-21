package com.example.guilda.controller;

import com.example.guilda.domain.aventura.Companheiro;
import com.example.guilda.domain.aventura.Especie;
import com.example.guilda.dto.aventura.CompanheiroDTO;
import com.example.guilda.dto.aventura.CompanheiroRequestDTO;
import com.example.guilda.dto.aventura.ErroResponse;
import com.example.guilda.service.CompanheiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aventureiro/{id}/companheiro")
@RequiredArgsConstructor
public class CompanheiroController {

    private final CompanheiroService service;

    // criar ou substituir
    @PutMapping
    public ResponseEntity<CompanheiroDTO> definir(
            @PathVariable Long id,
            @RequestBody @Valid CompanheiroRequestDTO dto) {

        return ResponseEntity.ok(service.definirOuSubstituir(id, dto));
    }

    // apaga
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}