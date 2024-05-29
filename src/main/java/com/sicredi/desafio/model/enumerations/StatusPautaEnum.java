package com.sicredi.desafio.model.enumerations;

public enum StatusPautaEnum {
    ABERTA("Pauta aberta para votação"),
    FECHADA("Pauta fechada"),
    CANCELADA("Pauta cancelada"),
    CRIADA("Pauta criada");

    private final String descricao;

    StatusPautaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
