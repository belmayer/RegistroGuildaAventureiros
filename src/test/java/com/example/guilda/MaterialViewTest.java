package com.example.guilda;

import com.example.guilda.operacoes.MaterialView;
import com.example.guilda.operacoes.MaterialViewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MaterialViewTest {

    @Autowired
    private MaterialViewService service;

    @Test
    void deveRetornarTop10MissoesDosUltimos15Dias() {

        // executa o método
        List<MaterialView> resultado = service.buscarTopMissoes15Dias();

        // validações básicas
        assertNotNull(resultado);
        assertTrue(resultado.size() <= 10);

        // imprime no console (IMPORTANTE PARA O PRINT)
        System.out.println("=== RESULTADO DAS MISSÕES ===");

        for (MaterialView missao : resultado) {
            System.out.println("ID: " + missao.getMissaoId());
            System.out.println("Título: " + missao.getTitulo());
            System.out.println("Índice: " + missao.getIndiceProntidao());
            System.out.println("Última atualização: " + missao.getUltimaAtualizacao());
            System.out.println("-----------------------------");
        }
    }
}