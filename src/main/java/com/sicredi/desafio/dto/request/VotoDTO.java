package com.sicredi.desafio.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotoDTO {

    @NotNull
    private Long associadoId;

    @NotNull
    private Long pautaOpcaoId;

}
