package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.response.ResultadoVotacaoDTO;
import com.sicredi.desafio.dto.request.VotoDTO;

public interface PautaVotacaoService {

    void registrarVoto(Long pautaId, VotoDTO votoDTO);

    ResultadoVotacaoDTO obterResultado(Long pautaId);
}
