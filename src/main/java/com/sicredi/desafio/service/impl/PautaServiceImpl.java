package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.PautaOpcao;
import com.sicredi.desafio.model.enumerations.StatusPautaEnum;
import com.sicredi.desafio.repository.PautaRepository;
import com.sicredi.desafio.service.PautaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class PautaServiceImpl implements PautaService {

    private final PautaRepository pautaRepository;

    private final ScheduledExecutorService scheduler;

    @Override
    public Pauta criarPauta(Pauta pauta) {

        for (PautaOpcao opcao : pauta.getOpcoes()) {
            opcao.setPauta(pauta);
        }

        pauta.setStatus(StatusPautaEnum.CRIADA);
        return pautaRepository.save(pauta);
    }

    @Override
    public Pauta abrirSessaoVotacao(Long pautaId, Long duracaoEmMinutos) {
        Optional<Pauta> pautaOptional = pautaRepository.findById(pautaId);
        if (pautaOptional.isPresent()) {
            Pauta pauta = pautaOptional.get();
            if (!StatusPautaEnum.CRIADA.equals(pauta.getStatus())) {
                throw new RuntimeException("A pauta já foi iniciada ou está fechada.");
            }
            pauta.setStatus(StatusPautaEnum.ABERTA);
            pauta.setDuracaoEmMinutos(duracaoEmMinutos != null ? duracaoEmMinutos : 1);
            pauta.setDataAtualizacao(LocalDateTime.now());
            agendarFechamentoSessao(pauta);

            return pautaRepository.save(pauta);
        }
        throw new RuntimeException("Pauta não encontrada.");
    }

    private void agendarFechamentoSessao(Pauta pauta) {
        LocalDateTime dataFim = pauta.getDataAtualizacao().plusMinutes(pauta.getDuracaoEmMinutos());
        long delay = dataFim.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - System.currentTimeMillis();

        scheduler.schedule(() -> {
            pauta.setStatus(StatusPautaEnum.FECHADA);
            pautaRepository.save(pauta);
        }, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void cancelarPauta(Long pautaId) {
        Optional<Pauta> pautaOptional = pautaRepository.findById(pautaId);
        Pauta pauta = pautaOptional.get();
        if (StatusPautaEnum.ABERTA.equals(pauta.getStatus()) || StatusPautaEnum.CRIADA.equals(pauta.getStatus())) {
            pauta.setStatus(StatusPautaEnum.CANCELADA);
            pauta.setDataAtualizacao(LocalDateTime.now());
            pautaRepository.save(pauta);
        } else {
            throw new RuntimeException("A pauta não está aberta nem criada.");
        }

    }

}
