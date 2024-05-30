package com.sicredi.desafio.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.desafio.controller.PautaController;
import com.sicredi.desafio.dto.request.AbrirSessaoVotacaoDTO;
import com.sicredi.desafio.dto.request.PautaDTO;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.service.PautaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PautaController.class)
@ActiveProfiles("test")
public class PautaPerformanceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PautaService pautaService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Título Teste");
        pauta.setDescricao("Descrição Teste");

        Mockito.when(pautaService.criarPauta(any(PautaDTO.class))).thenReturn(pauta);
    }

    @Test
    public void testCriarPautaPerformance() throws InterruptedException {
        int numberOfThreads = 100;
        int numberOfRequests = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        Runnable task = getCriarPautaRunnable();

        for (int i = 0; i < numberOfRequests; i++) {
            executorService.submit(task);
        }

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(10, TimeUnit.MINUTES);
        if (!terminated) {
            throw new RuntimeException("Timeout: Threads não finalizaram dentro do tempo esperado");
        }
    }

    @Test
    public void testAbrirSessaoVotacaoPerformance() throws InterruptedException {
        int numberOfThreads = 100;
        int numberOfRequests = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        Runnable task = getAbrirSessaoVotacaoRunnable();

        for (int i = 0; i < numberOfRequests; i++) {
            executorService.submit(task);
        }

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(10, TimeUnit.MINUTES);
        if (!terminated) {
            throw new RuntimeException("Timeout: Threads não finalizaram dentro do tempo esperado");
        }
    }

    @Test
    public void testCancelarPautaPerformance() throws InterruptedException {
        int numberOfThreads = 100;
        int numberOfRequests = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        Runnable task = getCancelarPautaRunnable();

        for (int i = 0; i < numberOfRequests; i++) {
            executorService.submit(task);
        }

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(10, TimeUnit.MINUTES);
        if (!terminated) {
            throw new RuntimeException("Timeout: Threads não finalizaram dentro do tempo esperado");
        }
    }

    private Runnable getCriarPautaRunnable() {
        PautaDTO pautaDTO = new PautaDTO();
        pautaDTO.setTitulo("Título Teste");
        pautaDTO.setDescricao("Descrição Teste");

        return () -> {
            try {
                mockMvc.perform(post("/pauta/v1/criar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pautaDTO)))
                        .andExpect(status().isCreated());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private Runnable getAbrirSessaoVotacaoRunnable() {
        Long pautaId = 4L;
        AbrirSessaoVotacaoDTO dto = new AbrirSessaoVotacaoDTO();
        dto.setDuracaoEmMinutos(1L);

        return () -> {
            try {
                mockMvc.perform(post("/pauta/v1/abrir-sessao/{pautaId}", pautaId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private Runnable getCancelarPautaRunnable() {
        Long pautaId = 1L;

        return () -> {
            try {
                mockMvc.perform(put("/pauta/v1/cancelar/{pautaId}", pautaId))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}