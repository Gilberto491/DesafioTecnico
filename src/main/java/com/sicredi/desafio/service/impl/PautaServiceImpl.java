package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.dto.request.PautaDTO;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.PautaOpcao;
import com.sicredi.desafio.model.enumerations.StatusPautaEnum;
import com.sicredi.desafio.repository.PautaRepository;
import com.sicredi.desafio.service.PautaService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@EnableScheduling
public class PautaServiceImpl implements PautaService {

    private final PautaRepository pautaRepository;

    private final ScheduledExecutorService scheduler;

    private final SessaoFechamentoService sessaoFechamentoService;

    @Override
    public Pauta criarPauta(PautaDTO pautaDTO) {

        Pauta pauta = new Pauta();
        pauta.setTitulo(pautaDTO.getTitulo());
        pauta.setDescricao(pautaDTO.getDescricao());
        pauta.setStatus(StatusPautaEnum.CRIADA);

        List<PautaOpcao> opcoes = pautaDTO.getOpcoes().stream().map(opcaoDTO -> {
            PautaOpcao opcao = new PautaOpcao();
            opcao.setTitulo(opcaoDTO.getTitulo());
            opcao.setPauta(pauta);
            return opcao;
        }).collect(Collectors.toList());

        pauta.setOpcoes(opcoes);

        return pautaRepository.save(pauta);
    }

    @Override
    @Transactional
    public Pauta abrirSessaoVotacao(Long pautaId, Long duracaoEmMinutos) {
        Optional<Pauta> pautaOptional = pautaRepository.findById(pautaId);
        if (pautaOptional.isPresent()) {
            Pauta pauta = pautaOptional.get();
            if (!StatusPautaEnum.CRIADA.equals(pauta.getStatus())) {
                throw new RuntimeException("A pauta já foi iniciada ou está fechada.");
            }

            pauta.setStatus(StatusPautaEnum.ABERTA);
            duracaoEmMinutos = duracaoEmMinutos != null ? duracaoEmMinutos : 1;
            pauta.setDataAtualizacao(LocalDateTime.now());
            agendarFechamentoSessao(pauta, duracaoEmMinutos);

            return pautaRepository.save(pauta);
        }
        throw new RuntimeException("Pauta não encontrada.");
    }

    public void agendarFechamentoSessao(Pauta pauta, long duracaoEmMinutos) {
        LocalDateTime dataFim = pauta.getDataAtualizacao().plusMinutes(duracaoEmMinutos);
        long delay = dataFim.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - System.currentTimeMillis();

        scheduler.schedule(() -> sessaoFechamentoService.fecharSessao(pauta.getId()), delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void cancelarPauta(Long pautaId) {

        Optional<Pauta> pautaOptional = pautaRepository.findById(pautaId);
        Pauta pauta = pautaOptional.orElseThrow(()
                -> new NoSuchElementException("Pauta não encontrada"));

        if (StatusPautaEnum.ABERTA.equals(pauta.getStatus()) || StatusPautaEnum.CRIADA.equals(pauta.getStatus())) {
            pauta.setStatus(StatusPautaEnum.CANCELADA);
            pauta.setDataAtualizacao(LocalDateTime.now());
            pautaRepository.save(pauta);
        } else {
            throw new RuntimeException("A pauta não está aberta nem criada.");
        }

    }

}
