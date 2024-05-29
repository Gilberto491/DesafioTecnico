package com.sicredi.desafio.repository;

import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.model.PautaOpcao;
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
public class PautaOpcaoRepositoryTest {

    @Autowired
    private PautaOpcaoRepository pautaOpcaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Test
    public void deveSalvarEEncontrarPautaOpcao() {
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

        pautaOpcaoRepository.save(pautaOpcao);

        List<PautaOpcao> opcoes = pautaOpcaoRepository.findAll();
        assertThat(opcoes).hasSize(1);
        assertThat(opcoes.get(0).getTitulo()).isEqualTo("Opcao 1");
        assertThat(opcoes.get(0).getPauta().getTitulo()).isEqualTo("Teste Pauta");
    }

}
