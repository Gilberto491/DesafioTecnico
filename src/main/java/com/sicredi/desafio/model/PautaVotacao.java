package com.sicredi.desafio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PautaVotacao extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "associadoId")
    private Associado associado;

    @ManyToOne
    @JoinColumn(name = "pautaOpcoesId")
    private PautaOpcao pautaOpcao;

}
