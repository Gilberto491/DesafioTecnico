package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.request.PautaDTO;
import com.sicredi.desafio.model.Pauta;

public interface PautaService {

    Pauta criarPauta(PautaDTO pautaDTO);

    Pauta abrirSessaoVotacao(Long pautaId, Long duracaoEmMinutos);

    void cancelarPauta(Long pautaId);
}
