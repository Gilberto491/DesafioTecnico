package com.sicredi.desafio.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PautaDTO {

    @NotEmpty
    private String titulo;

    @NotEmpty
    private String descricao;

    @NotEmpty
    private List<PautaOpcaoDTO> opcoes;
}
