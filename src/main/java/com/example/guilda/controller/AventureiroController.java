package com.example.guilda.controller;

import com.example.guilda.domain.aventura.Aventureiro;
import com.example.guilda.domain.aventura.ClasseAventureiro;
import com.example.guilda.dto.aventura.AventureiroDTO;
import com.example.guilda.dto.aventura.AventureiroRequestDTO;
import com.example.guilda.service.AventureiroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/aventureiro")
@RequiredArgsConstructor
public class AventureiroController {

    private final AventureiroService service;

    @GetMapping("/{id}")
    public AventureiroDTO buscar(@PathVariable Long id) {
        return service.buscarDTO(id);
    }

    //  filtros + paginação + headers
    @GetMapping
    public ResponseEntity<Page<AventureiroDTO>> listar(
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) ClasseAventureiro classe,
            @RequestParam(required = false) Integer nivelMin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        if (page < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "page não pode ser negativo"
            );
        }

        if (size < 1 || size > 50) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "size deve estar entre 1 e 50"
            );
        }

        Page<AventureiroDTO> pageResult = service.listar(ativo, classe, nivelMin, page, size);

        // headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(pageResult.getTotalElements()));
        headers.add("X-Page", String.valueOf(pageResult.getNumber()));
        headers.add("X-Size", String.valueOf(pageResult.getSize()));
        headers.add("X-Total-Pages", String.valueOf(pageResult.getTotalPages()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(pageResult);
    }

    @PatchMapping("/{id}/inativar")
    public void inativar(@PathVariable Long id) {
        service.inativar(id);
    }

    @PatchMapping("/{id}/reativar")
    public void reativar(@PathVariable Long id) {
        service.reativar(id);
    }

    // criar aventureiros
    // alterado para consumir o dto e não a entidade
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AventureiroDTO criar(@RequestBody @Valid AventureiroRequestDTO dto) {
        return service.criar(dto);
    }

    // atualizar
    // também alterado para fazer o mesmo
    @PutMapping("/{id}")
    public AventureiroDTO atualizar(@PathVariable Long id,
                                    @RequestBody @Valid AventureiroRequestDTO dto) {
        return service.atualizar(id, dto);
    }
}