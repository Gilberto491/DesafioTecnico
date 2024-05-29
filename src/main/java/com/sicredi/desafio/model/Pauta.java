package com.sicredi.desafio.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sicredi.desafio.model.enumerations.StatusPautaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pauta extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusPautaEnum status;

    private Long duracaoEmMinutos;

    @OneToMany(mappedBy = "pauta", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PautaOpcao> opcoes;

}
