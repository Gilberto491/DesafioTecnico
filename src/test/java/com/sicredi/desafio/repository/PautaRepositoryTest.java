package com.sicredi.desafio.repository;

import com.sicredi.desafio.model.Pauta;
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
public class PautaRepositoryTest {

    @Autowired
    private PautaRepository pautaRepository;

    @Test
    public void deveSalvarEEncontrarPauta() {
        Pauta pauta = new Pauta();
        pauta.setTitulo("Test Pauta");
        pauta.setDescricao("Descricao Teste");
        pauta.setStatus(StatusPautaEnum.ABERTA);
        pauta.setDataCriacao(LocalDateTime.now());

        pautaRepository.save(pauta);

        List<Pauta> pautas = pautaRepository.findAll();
        assertThat(pautas).hasSize(1);
        assertThat(pautas.get(0).getTitulo()).isEqualTo("Test Pauta");
    }

}
