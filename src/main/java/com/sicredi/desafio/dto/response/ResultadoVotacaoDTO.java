package com.sicredi.desafio.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ResultadoVotacaoDTO {

    private Long pautaId;

    private String tituloPauta;

    private Map<String, Long> resultados;
}
