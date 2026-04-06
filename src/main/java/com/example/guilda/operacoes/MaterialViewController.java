package com.example.guilda.operacoes;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/missoes")
public class MaterialViewController {

    private final MaterialViewService service;

    public MaterialViewController(MaterialViewService service) {
        this.service = service;
    }

    @GetMapping("/top15dias")
    public List<MaterialView> buscarTopMissoes() {
        return service.buscarTopMissoes15Dias();
    }
}