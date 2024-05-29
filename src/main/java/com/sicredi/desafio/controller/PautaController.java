package com.sicredi.desafio.controller;

import com.sicredi.desafio.exception.PautaException;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.service.PautaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pauta/v1")
@AllArgsConstructor
public class PautaController {

    private static final Logger logger = LoggerFactory.getLogger(PautaController.class);
    private final PautaService pautaService;

    @PostMapping("/criar")
    public ResponseEntity<Pauta> criarPauta(@Valid @RequestBody Pauta pauta) {
        try {
            Pauta novaPauta = pautaService.criarPauta(pauta);
            logger.info("Pauta criada com sucesso: {}", novaPauta);
            return new ResponseEntity<>(novaPauta, HttpStatus.CREATED);
        } catch (PautaException e) {
            logger.error("Erro ao criar pauta", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{pautaId}/abrir-sessao")
    public ResponseEntity<?> abrirSessaoVotacao(@PathVariable Long pautaId, @RequestParam(required = false) Long duracaoEmMinutos) {
        try {
            Pauta pauta = pautaService.abrirSessaoVotacao(pautaId, duracaoEmMinutos);
            logger.info("Pauta aberta com sucesso: {}", pauta);
            return new ResponseEntity<>(pauta, HttpStatus.OK);
        } catch (PautaException e) {
            logger.error("Erro ao abrir sessão de votação para a pauta: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cancelar/{pautaId}")
    public ResponseEntity<?> cancelarPauta(@PathVariable Long pautaId) {
        try {
            pautaService.cancelarPauta(pautaId);
            logger.info("Pauta cancelada com sucesso");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (PautaException e) {
            logger.error("Erro ao cancelar a pauta: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
