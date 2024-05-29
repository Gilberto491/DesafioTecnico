package com.sicredi.desafio.service;

import com.sicredi.desafio.model.Pauta;

public interface PautaService {

    Pauta criarPauta(Pauta pauta);

    Pauta abrirSessaoVotacao(Long pautaId, Long duracaoEmMinutos);

    void cancelarPauta(Long pautaId);
}
