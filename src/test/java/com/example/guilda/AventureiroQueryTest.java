package com.example.guilda;

import com.example.guilda.domain.audit.Organizacao;
import com.example.guilda.domain.audit.Usuario;
import com.example.guilda.domain.aventura.*;
import com.example.guilda.dto.aventura.MissaoRelatorioDTO;
import com.example.guilda.dto.aventura.RankingDTO;
import com.example.guilda.repository.audit.OrganizacaoRepository;
import com.example.guilda.repository.audit.UsuarioRepository;
import com.example.guilda.repository.aventura.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AventureiroQueryTest {

    @Autowired
    private AventureiroRepository aventureiroRepository;

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ParticipacaoMissaoRepository participacaoRepository;

    @Autowired
    private MissaoRepository missaoRepository;

    // 🔹 CRIA BASE
    private Aventureiro criarAventureiroBase() {

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
                        .email("admin@test.com")
                        .senhaHash("123")
                        .status("ATIVO")
                        .organizacao(org)
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .build()
        );

        return aventureiroRepository.save(
                Aventureiro.builder()
                        .nome("Arthas")
                        .classe(ClasseAventureiro.GUERREIRO)
                        .nivel(10)
                        .ativo(true)
                        .organizacao(org)
                        .usuario(user)
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .build()
        );
    }

    // 🧪 TESTE 1 — FILTRO
    @Test
    void deveFiltrarAventureiros() {

        criarAventureiroBase();

        var resultado = aventureiroRepository
                .findByAtivoAndClasseAndNivelGreaterThanEqual(
                        true,
                        ClasseAventureiro.GUERREIRO,
                        5,
                        PageRequest.of(0, 10)
                );

        assertThat(resultado.getContent()).hasSize(1);
    }

    // 🧪 TESTE 2 — BUSCA POR NOME
    @Test
    void deveBuscarPorNome() {

        criarAventureiroBase();

        var resultado = aventureiroRepository
                .findByNomeContainingIgnoreCase(
                        "arth",
                        PageRequest.of(0, 10)
                );

        assertThat(resultado.getContent()).hasSize(1);
    }

    // 🧪 TESTE 3 — RANKING
    @Test
    void deveGerarRanking() {

        Aventureiro aventureiro = criarAventureiroBase();

        Missao missao = missaoRepository.save(
                Missao.builder()
                        .titulo("Missão Teste")
                        .organizacao(aventureiro.getOrganizacao())
                        .nivelPerigo(NivelPerigo.MEDIO)
                        .status(StatusMissao.CONCLUIDA)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        participacaoRepository.save(
                ParticipacaoMissao.builder()
                        .aventureiro(aventureiro)
                        .missao(missao)
                        .papel("LIDER") // 🔥 ADICIONE ISSO
                        .recompensa(100)
                        .destaque(true)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        List<RankingDTO> ranking = participacaoRepository.ranking(
                OffsetDateTime.now().minusDays(1),
                OffsetDateTime.now().plusDays(1)
        );

        assertThat(ranking).isNotEmpty();
        assertThat(ranking.get(0).totalParticipacoes()).isEqualTo(1);
    }

    // 🧪 TESTE 4 — MISSÃO SEM PARTICIPANTE
    @Test
    void deveRetornarMissaoSemParticipantes() {

        Aventureiro aventureiro = criarAventureiroBase();

        Missao missao = missaoRepository.save(
                Missao.builder()
                        .titulo("Missão Vazia")
                        .organizacao(aventureiro.getOrganizacao())
                        .nivelPerigo(NivelPerigo.BAIXO)
                        .status(StatusMissao.PLANEJADA)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        var participantes = participacaoRepository.findAll()
                .stream()
                .filter(p -> p.getMissao().getId().equals(missao.getId()))
                .toList();

        assertThat(participantes).isEmpty();
    }

    // 🧪 TESTE 5 — RELATÓRIO
    @Test
    void deveGerarRelatorioMissoes() {

        Aventureiro aventureiro = criarAventureiroBase();

        Missao missao = missaoRepository.save(
                Missao.builder()
                        .titulo("Missão Relatório")
                        .organizacao(aventureiro.getOrganizacao())
                        .nivelPerigo(NivelPerigo.ALTO)
                        .status(StatusMissao.CONCLUIDA)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        participacaoRepository.save(
                ParticipacaoMissao.builder()
                        .aventureiro(aventureiro)
                        .missao(missao)
                        .papel("LIDER") // 🔥 ESSENCIAL
                        .recompensa(200)
                        .destaque(false)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        List<MissaoRelatorioDTO> resultado = missaoRepository.relatorio(
                OffsetDateTime.now().minusDays(1),
                OffsetDateTime.now().plusDays(1)
        );

        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).totalParticipantes()).isEqualTo(1);
    }

    // 🧪 TESTE 6 — AVENTUREIRO SEM MISSÃO
    @Test
    void deveRetornarAventureiroSemMissao() {

        Aventureiro aventureiro = criarAventureiroBase();

        Long total = participacaoRepository.contarParticipacoes(aventureiro.getId());

        assertThat(total).isEqualTo(0);
    }


    // teste novo
    @Test
    void deveBuscarPerfilCompleto() {

        Aventureiro aventureiro = criarAventureiroBase();

        Aventureiro resultado = aventureiroRepository.buscarCompleto(aventureiro.getId());

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(aventureiro.getId());
    }

    @Test
    void deveListarMissoesComFiltro() {

        Aventureiro aventureiro = criarAventureiroBase();

        Missao missao = missaoRepository.save(
                Missao.builder()
                        .titulo("Missão Filtro")
                        .organizacao(aventureiro.getOrganizacao())
                        .nivelPerigo(NivelPerigo.MEDIO)
                        .status(StatusMissao.PLANEJADA)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        var resultado = missaoRepository
                .findByStatusAndNivelPerigoAndCreatedAtBetween(
                        StatusMissao.PLANEJADA,
                        NivelPerigo.MEDIO,
                        OffsetDateTime.now().minusDays(1),
                        OffsetDateTime.now().plusDays(1),
                        PageRequest.of(0, 10)
                );

        assertThat(resultado.getContent()).isNotEmpty();
    }

    @Test
    void deveRetornarMissaoComParticipantes() {

        Aventureiro aventureiro = criarAventureiroBase();

        Missao missao = missaoRepository.save(
                Missao.builder()
                        .titulo("Missão Com Participante")
                        .organizacao(aventureiro.getOrganizacao())
                        .nivelPerigo(NivelPerigo.MEDIO)
                        .status(StatusMissao.CONCLUIDA)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        participacaoRepository.save(
                ParticipacaoMissao.builder()
                        .aventureiro(aventureiro)
                        .missao(missao)
                        .papel("LIDER")
                        .recompensa(100)
                        .destaque(true)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        var participantes = missaoRepository.buscarParticipantes(missao.getId());

        assertThat(participantes).isNotEmpty();
    }

    @Test
    void deveRetornarBuscaVazia() {

        var resultado = aventureiroRepository
                .findByNomeContainingIgnoreCase(
                        "naoexiste",
                        PageRequest.of(0, 10)
                );

        assertThat(resultado.getContent()).isEmpty();
    }

    @Test
    void deveRetornarRankingVazio() {

        List<RankingDTO> ranking = participacaoRepository.ranking(null, null);

        assertThat(ranking).isEmpty();
    }
}