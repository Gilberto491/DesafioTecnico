package com.sicredi.desafio.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PautaOpcaoDTO {

    @NotEmpty
    private String titulo;
}
