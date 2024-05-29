package com.sicredi.desafio.repository;

import com.sicredi.desafio.model.Associado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class AssociadoRepositoryTest {

    @Autowired
    private AssociadoRepository associadoRepository;

    @Test
    public void deveSalvarEEncontrarAssociado() {
        Associado associado = new Associado();
        associado.setCpf("12345678910");
        associado.setNome("Teste Associado");
        associado.setDataCriacao(LocalDateTime.now());

        associado = associadoRepository.save(associado);

        List<Associado> associados = associadoRepository.findAll();
        assertThat(associados).hasSize(1);
        assertThat(associados.get(0).getCpf()).isEqualTo("12345678910");
        assertThat(associados.get(0).getNome()).isEqualTo("Teste Associado");
    }


}
