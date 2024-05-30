package com.sicredi.desafio.controller;

import com.sicredi.desafio.dto.response.ResultadoVotacaoDTO;
import com.sicredi.desafio.dto.request.VotoDTO;
import com.sicredi.desafio.service.PautaVotacaoService;
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
@RequestMapping("/votacao/v1")
@AllArgsConstructor
@Validated
public class PautaVotacaoController {

    private static final Logger logger = LoggerFactory.getLogger(PautaVotacaoController.class);
    private final PautaVotacaoService pautaVotacaoService;

    @PostMapping("/votar/{pautaId}")
    public ResponseEntity<?> votar(@PathVariable Long pautaId, @Valid @RequestBody VotoDTO votoDTO) {
        pautaVotacaoService.registrarVoto(pautaId, votoDTO);
        logger.info("Voto registrado com sucesso para associado: {}", votoDTO.getAssociadoId());
        HashMap<String, String> resposta = new HashMap<>();
        resposta.put("message", "Voto registrado com sucesso");
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @GetMapping("/resultado/{pautaId}")
    public ResponseEntity<ResultadoVotacaoDTO> obterResultado(@PathVariable Long pautaId) {
        ResultadoVotacaoDTO resultado = pautaVotacaoService.obterResultado(pautaId);
        logger.info("Resultado da votação obtido com sucesso para pauta: {}", pautaId);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

}
