package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.enumerations.StatusPautaEnum;
import com.sicredi.desafio.repository.PautaRepository;
import com.sicredi.desafio.service.PautaService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PautaServiceImpl implements PautaService {

    private final PautaRepository pautaRepository;

    @Override
    public Pauta criarPauta(Pauta pauta) {
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
            return pautaRepository.save(pauta);
        }
        throw new RuntimeException("Pauta não encontrada.");
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

    @Scheduled(fixedRate = 60000)
    public void fecharSessoesExpiradas() {
        List<Pauta> pautas = pautaRepository.findAllByStatus(StatusPautaEnum.ABERTA);
        LocalDateTime now = LocalDateTime.now();
        for (Pauta pauta : pautas) {
            LocalDateTime dataInicio = pauta.getDataAtualizacao();
            LocalDateTime dataFim = dataInicio.plusMinutes(pauta.getDuracaoEmMinutos());
            if (now.isAfter(dataFim)) {
                pauta.setStatus(StatusPautaEnum.FECHADA);
                pautaRepository.save(pauta);
            }
        }
    }

}
