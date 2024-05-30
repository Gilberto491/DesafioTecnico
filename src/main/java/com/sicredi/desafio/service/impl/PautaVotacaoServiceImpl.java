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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PautaVotacaoServiceImpl implements PautaVotacaoService {

    private final PautaVotacaoRepository pautaVotacaoRepository;
    private final PautaOpcaoRepository pautaOpcaoRepository;
    private final AssociadoRepository associadoRepository;
    private final PautaRepository pautaRepository;

    @Override
    public void registrarVoto(Long pautaId, VotoDTO votoDTO) {
        Optional<Associado> associadoOpt = associadoRepository.findById(votoDTO.getAssociadoId());
        Optional<PautaOpcao> pautaOpcoesOpt = pautaOpcaoRepository.findById(votoDTO.getPautaOpcaoId());

        PautaOpcao pautaOpcoes = pautaOpcoesOpt.get();

        validarPautaAberta(pautaOpcoes.getPauta());
        validarAssociadoOuOpcaoPauta(associadoOpt);
        validarOpcaoPautaPertencePautaEspecificada(pautaOpcoes.getPauta(), pautaId);
        validarAssociadoJaVotou(votoDTO);

        PautaVotacao novoVoto = new PautaVotacao();
        novoVoto.setAssociado(associadoOpt.get());
        novoVoto.setPautaOpcao(pautaOpcoesOpt.get());
        novoVoto.setDataAtualizacao(LocalDateTime.now());

        pautaVotacaoRepository.save(novoVoto);

        pautaOpcoes.setVotosRecebidos(pautaOpcoes.getVotosRecebidos() + 1);
        pautaOpcoes.setDataAtualizacao(LocalDateTime.now());
        pautaOpcaoRepository.save(pautaOpcoes);

    }

    private void validarPautaAberta(Pauta pauta) {
        if (!StatusPautaEnum.ABERTA.equals(pauta.getStatus())) {
            throw new IllegalArgumentException("Você só deve votar em uma pauta aberta");
        }
    }

    private void validarAssociadoOuOpcaoPauta(Optional<Associado> associadoOpt) {
        if (associadoOpt.isEmpty()) {
            throw new IllegalArgumentException("Associado ou opção de pauta inválida");
        }
    }

    private void validarOpcaoPautaPertencePautaEspecificada(Pauta pauta, Long pautaId) {
        if (pauta.getId().equals(pautaId)) {
            throw new IllegalArgumentException("A opção de pauta não pertence à pauta especificada");
        }
    }

    private void validarAssociadoJaVotou(VotoDTO votoDTO) {
        boolean jaVotou = pautaVotacaoRepository.existsByAssociadoIdAndPautaOpcaoId(
                votoDTO.getAssociadoId(), votoDTO.getPautaOpcaoId());
        if (jaVotou) {
            throw new IllegalArgumentException("Associado já votou nesta pauta");
        }
    }

    @Override
    public ResultadoVotacaoDTO obterResultado(Long pautaId) {
        List<PautaOpcao> opcoes = pautaOpcaoRepository.findByPautaId(pautaId);
        Map<String, Long> resultado = new HashMap<>();

        for (PautaOpcao opcao : opcoes) {
            resultado.put(opcao.getTitulo(), opcao.getVotosRecebidos());
        }

        Pauta pauta = pautaRepository.findById(pautaId).orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada"));
        validarPautaFechada(pauta);

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
