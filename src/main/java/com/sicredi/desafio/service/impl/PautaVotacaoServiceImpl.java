package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.dto.request.VotoDTO;
import com.sicredi.desafio.dto.response.ResultadoVotacaoDTO;
import com.sicredi.desafio.model.Associado;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.PautaOpcao;
import com.sicredi.desafio.model.PautaVotacao;
import com.sicredi.desafio.model.enumerations.StatusPautaEnum;
import com.sicredi.desafio.repository.AssociadoRepository;
import com.sicredi.desafio.repository.PautaOpcaoRepository;
import com.sicredi.desafio.repository.PautaRepository;
import com.sicredi.desafio.repository.PautaVotacaoRepository;
import com.sicredi.desafio.service.PautaVotacaoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PautaVotacaoServiceImpl implements PautaVotacaoService {

    @PersistenceContext
    private EntityManager entityManager;
    private final PautaVotacaoRepository pautaVotacaoRepository;
    private final PautaOpcaoRepository pautaOpcaoRepository;
    private final AssociadoRepository associadoRepository;
    private final PautaRepository pautaRepository;

    @Override
    @Transactional
    public void registrarVoto(Long pautaId, VotoDTO votoDTO) {

        Associado associado = buscarAssociado(votoDTO.getAssociadoId());
        PautaOpcao pautaOpcao = buscarPautaOpcao(votoDTO.getPautaOpcaoId());
        Pauta pauta = buscarPauta(pautaId);

        entityManager.clear();

        validarPautaAberta(pauta);
        validarOpcaoPautaPertencePautaEspecificada(pautaOpcao.getPauta(), pautaId);
        validarAssociadoJaVotou(votoDTO.getAssociadoId(), pautaId);

        PautaVotacao novoVoto = new PautaVotacao();
        novoVoto.setAssociado(associado);
        novoVoto.setPautaOpcao(pautaOpcao);
        novoVoto.setDataAtualizacao(LocalDateTime.now());

        pautaVotacaoRepository.save(novoVoto);

        pautaOpcao.setVotosRecebidos(pautaOpcao.getVotosRecebidos() + 1);
        pautaOpcao.setDataAtualizacao(LocalDateTime.now());
        pautaOpcaoRepository.save(pautaOpcao);

        entityManager.flush();

    }

    private Associado buscarAssociado(Long associadoId) {
        return associadoRepository.findById(associadoId)
                .orElseThrow(() -> new NoSuchElementException("Associado não encontrado"));
    }

    private PautaOpcao buscarPautaOpcao(Long pautaOpcaoId) {
        return pautaOpcaoRepository.findById(pautaOpcaoId)
                .orElseThrow(() -> new NoSuchElementException("Opção de pauta não encontrada"));
    }

    private Pauta buscarPauta(Long pautaId) {
        return pautaRepository.findById(pautaId)
                .orElseThrow(() -> new NoSuchElementException("Pauta não encontrada"));
    }

    private void validarPautaAberta(Pauta pauta) {
        if (!StatusPautaEnum.ABERTA.equals(pauta.getStatus())) {
            throw new IllegalArgumentException("Você só deve votar em uma pauta aberta");
        }
    }

    private void validarOpcaoPautaPertencePautaEspecificada(Pauta pauta, Long pautaId) {
        if (!pauta.getId().equals(pautaId)) {
            throw new IllegalArgumentException("A opção de pauta não pertence à pauta especificada");
        }
    }

    private void validarAssociadoJaVotou(Long associadoId, Long pautaId) {
        boolean jaVotou = pautaVotacaoRepository.existsByAssociadoIdAndPautaOpcaoPautaId(
                associadoId, pautaId);
        if (jaVotou) {
            throw new IllegalArgumentException("Associado já votou nesta pauta");
        }
    }

    @Override
    public ResultadoVotacaoDTO obterResultado(Long pautaId) {
        List<PautaOpcao> opcoes = pautaOpcaoRepository.findByPautaId(pautaId);
        Map<String, Long> resultado = new HashMap<>();

        Pauta pauta = pautaRepository.findById(pautaId).orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada"));
        validarPautaFechada(pauta);

        for (PautaOpcao opcao : opcoes) {
            resultado.put(opcao.getTitulo(), opcao.getVotosRecebidos());
        }

        ResultadoVotacaoDTO resultadoVotacaoDTO = new ResultadoVotacaoDTO();
        resultadoVotacaoDTO.setPautaId(pautaId);
        resultadoVotacaoDTO.setTituloPauta(pauta.getTitulo());
        resultadoVotacaoDTO.setResultados(resultado);

        return resultadoVotacaoDTO;
    }

    private void validarPautaFechada(Pauta pauta) {
        if (!pauta.getStatus().equals(StatusPautaEnum.FECHADA)) {
            throw new IllegalArgumentException("Não é possivel visualizar o resultado pois a votação ainda não foi encerrada");
        }
    }

}
