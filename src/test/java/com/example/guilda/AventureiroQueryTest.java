package com.example.guilda;

import com.example.guilda.domain.audit.Organizacao;
import com.example.guilda.domain.audit.Usuario;
import com.example.guilda.domain.aventura.Aventureiro;
import com.example.guilda.domain.aventura.ClasseAventureiro;
import com.example.guilda.repository.audit.OrganizacaoRepository;
import com.example.guilda.repository.audit.UsuarioRepository;
import com.example.guilda.repository.aventura.AventureiroRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AventureiroQueryTest {

    @Autowired
    private AventureiroRepository aventureiroRepository;

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🔹 BASE REUTILIZÁVEL
    private Aventureiro criar(String nome, ClasseAventureiro classe, int nivel, boolean ativo) {

        Organizacao org = organizacaoRepository.save(
                Organizacao.builder()
                        .nome("Guilda Teste")
                        .ativo(true)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        Usuario user = usuarioRepository.save(
                Usuario.builder()
                        .nome("Admin")
                        .email(nome + "@test.com")
                        .senhaHash("123")
                        .status("ATIVO")
                        .organizacao(org)
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .build()
        );

        return aventureiroRepository.save(
                Aventureiro.builder()
                        .nome(nome)
                        .classe(classe)
                        .nivel(nivel)
                        .ativo(ativo)
                        .organizacao(org)
                        .usuario(user)
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .build()
        );
    }

    // =========================
    // 🔹 FILTROS
    // =========================

    @Test
    void deveFiltrarPorAtivo() {
        criar("A", ClasseAventureiro.GUERREIRO, 10, true);
        criar("B", ClasseAventureiro.MAGO, 5, false);

        var result = aventureiroRepository.findByAtivo(true, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void deveFiltrarPorClasse() {
        criar("A", ClasseAventureiro.GUERREIRO, 10, true);
        criar("B", ClasseAventureiro.MAGO, 5, true);

        var result = aventureiroRepository.findByClasse(
                ClasseAventureiro.GUERREIRO,
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void deveFiltrarPorNivel() {
        criar("A", ClasseAventureiro.GUERREIRO, 10, true);
        criar("B", ClasseAventureiro.GUERREIRO, 5, true);

        var result = aventureiroRepository.findByNivelGreaterThanEqual(
                8,
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void deveFiltrarAtivoClasseNivel() {
        criar("A", ClasseAventureiro.GUERREIRO, 10, true);
        criar("B", ClasseAventureiro.GUERREIRO, 5, true);
        criar("C", ClasseAventureiro.GUERREIRO, 10, false);

        var result = aventureiroRepository
                .findByAtivoAndClasseAndNivelGreaterThanEqual(
                        true,
                        ClasseAventureiro.GUERREIRO,
                        8,
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void deveFiltrarAtivoENivel() {
        criar("A", ClasseAventureiro.GUERREIRO, 10, true);
        criar("B", ClasseAventureiro.GUERREIRO, 5, true);

        var result = aventureiroRepository
                .findByAtivoAndNivelGreaterThanEqual(true, 8, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void deveFiltrarClasseENivel() {
        criar("A", ClasseAventureiro.GUERREIRO, 10, true);
        criar("B", ClasseAventureiro.MAGO, 10, true);

        var result = aventureiroRepository
                .findByClasseAndNivelGreaterThanEqual(
                        ClasseAventureiro.GUERREIRO,
                        8,
                        PageRequest.of(0, 10)
                );

        assertThat(result.getContent()).hasSize(1);
    }

    // =========================
    // 🔹 BUSCA POR NOME
    // =========================

    @Test
    void deveBuscarPorNome() {
        criar("Arthas", ClasseAventureiro.GUERREIRO, 10, true);

        var result = aventureiroRepository
                .findByNomeContainingIgnoreCase("arth", PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void deveRetornarBuscaVazia() {
        var result = aventureiroRepository
                .findByNomeContainingIgnoreCase("naoexiste", PageRequest.of(0, 10));

        assertThat(result.getContent()).isEmpty();
    }

    // =========================
    // 🔹 QUERY CUSTOM
    // =========================

    @Test
    void deveBuscarPerfilCompleto() {

        Aventureiro aventureiro = criar("Gandalf", ClasseAventureiro.MAGO, 99, true);

        Aventureiro result =
                aventureiroRepository.buscarCompleto(aventureiro.getId());

        assertThat(result).isNotNull();
        assertThat(result.getOrganizacao()).isNotNull();
        assertThat(result.getUsuario()).isNotNull();
    }
}