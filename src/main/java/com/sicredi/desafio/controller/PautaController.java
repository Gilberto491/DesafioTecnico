package com.sicredi.desafio.controller;

import com.sicredi.desafio.dto.request.AbrirSessaoVotacaoDTO;
import com.sicredi.desafio.dto.request.PautaDTO;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.service.PautaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/pauta/v1")
@AllArgsConstructor
@Validated
public class PautaController {

    private static final Logger logger = LoggerFactory.getLogger(PautaController.class);
    private final PautaService pautaService;

    @PostMapping("/criar")
    public ResponseEntity<Pauta> criarPauta(@Valid @RequestBody PautaDTO pautaDTO) {
        Pauta novaPauta = pautaService.criarPauta(pautaDTO);
        logger.info("Pauta criada com sucesso: {}", novaPauta.getTitulo());
        return new ResponseEntity<>(novaPauta, HttpStatus.CREATED);
    }

    @PostMapping("/abrir-sessao/{pautaId}")
    public ResponseEntity<?> abrirSessaoVotacao(@PathVariable Long pautaId, @Valid @RequestBody AbrirSessaoVotacaoDTO duracaoEmMinutos) {
        Pauta pauta = pautaService.abrirSessaoVotacao(pautaId, duracaoEmMinutos.getDuracaoEmMinutos());
        logger.info("Pauta aberta com sucesso: {}", pauta.getTitulo());
        return new ResponseEntity<>(pauta, HttpStatus.OK);
    }

    @PutMapping("/cancelar/{pautaId}")
    public ResponseEntity<?> cancelarPauta(@PathVariable Long pautaId) {
        pautaService.cancelarPauta(pautaId);
        String info = "Pauta cancelada com sucesso";
        HashMap<String, String> resposta = new HashMap<>();
        resposta.put("message", info);
        logger.info(info);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

}
