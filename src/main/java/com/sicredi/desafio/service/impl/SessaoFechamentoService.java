package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.PautaOpcao;
import com.sicredi.desafio.model.enumerations.StatusPautaEnum;
import com.sicredi.desafio.repository.PautaOpcaoRepository;
import com.sicredi.desafio.repository.PautaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class SessaoFechamentoService {

    private final PautaRepository pautaRepository;
    private final PautaOpcaoRepository pautaOpcaoRepository;

    @Transactional
    public void fecharSessao(Long pautaId) {
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new NoSuchElementException("Pauta não encontrada"));

        List<PautaOpcao> opcoes = pautaOpcaoRepository.findByPautaId(pautaId);

        pauta.setStatus(StatusPautaEnum.FECHADA);
        pautaRepository.save(pauta);

        for (PautaOpcao opcao : opcoes) {
            opcao.setDataAtualizacao(LocalDateTime.now());
            pautaOpcaoRepository.save(opcao);
        }
    }
}
