package com.example.guilda.controller;

import com.example.guilda.domain.aventura.Companheiro;
import com.example.guilda.domain.aventura.Especie;
import com.example.guilda.dto.aventura.CompanheiroDTO;
import com.example.guilda.dto.aventura.ErroResponse;
import com.example.guilda.service.CompanheiroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aventureiro/{id}/companheiro")
@RequiredArgsConstructor
public class CompanheiroController {

    private final CompanheiroService service;

    // ✅ Criar ou substituir
    @PutMapping
    public ResponseEntity<?> definir(
            @PathVariable Long id,
            @RequestBody CompanheiroDTO dto) {

        try {
            // 🔥 converte DTO → entidade
            Companheiro companheiro = new Companheiro();
            companheiro.setNome(dto.getNome());

            try {
                companheiro.setEspecie(dto.getEspecie());
            } catch (Exception e) {
                throw new RuntimeException("especie inválida");
            }

            companheiro.setLealdade(dto.getLealdade());

            // chama service
            Companheiro salvo = service.definirOuSubstituir(id, companheiro);

            // 🔥 converte entidade → DTO (resposta)
            CompanheiroDTO response = CompanheiroDTO.builder()
                    .nome(salvo.getNome())
                    .especie(salvo.getEspecie())
                    .lealdade(salvo.getLealdade())
                    .build();

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new ErroResponse("Solicitação inválida", e.getMessage())
            );
        }
    }

    // ✅ Remover companheiro
    @DeleteMapping
    public ResponseEntity<?> remover(@PathVariable Long id) {

        try {
            service.remover(id);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new ErroResponse("Solicitação inválida", e.getMessage())
            );
        }
    }
}