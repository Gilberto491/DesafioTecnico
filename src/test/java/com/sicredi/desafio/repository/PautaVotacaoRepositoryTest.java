package com.sicredi.desafio.repository;

import com.sicredi.desafio.model.Associado;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.PautaOpcao;
import com.sicredi.desafio.model.PautaVotacao;
import com.sicredi.desafio.model.enumerations.StatusPautaEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PautaVotacaoRepositoryTest {

    @Autowired
    private PautaVotacaoRepository pautaVotacaoRepository;

    @Autowired
    private PautaOpcaoRepository pautaOpcaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Test
    public void deveSalvarEEncontrarPautaAssociadoVotacao() {
        Pauta pauta = new Pauta();
        pauta.setTitulo("Teste Pauta");
        pauta.setDescricao("Descricao Teste");
        pauta.setStatus(StatusPautaEnum.ABERTA);
        pauta.setDataCriacao(LocalDateTime.now());
        pauta = pautaRepository.save(pauta);

        PautaOpcao pautaOpcao = new PautaOpcao();
        pautaOpcao.setTitulo("Opcao 1");
        pautaOpcao.setPauta(pauta);
        pautaOpcao.setDataCriacao(LocalDateTime.now());
        pautaOpcao = pautaOpcaoRepository.save(pautaOpcao);

        Associado associado = new Associado();
        associado.setCpf("12345678910");
        associado.setNome("Teste Associado");
        associado.setDataCriacao(LocalDateTime.now());
        associado = associadoRepository.save(associado);

        PautaVotacao votacao = new PautaVotacao();
        votacao.setAssociado(associado);
        votacao.setPautaOpcao(pautaOpcao);
        votacao.setDataCriacao(LocalDateTime.now());
        pautaVotacaoRepository.save(votacao);

        List<PautaVotacao> votacoes = pautaVotacaoRepository.findAll();
        assertThat(votacoes).hasSize(1);
        assertThat(votacoes.get(0).getAssociado().getCpf()).isEqualTo("12345678910");
        assertThat(votacoes.get(0).getPautaOpcao().getTitulo()).isEqualTo("Opcao 1");
    }

}

