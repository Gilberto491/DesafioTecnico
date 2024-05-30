package com.sicredi.desafio.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.desafio.controller.PautaVotacaoController;
import com.sicredi.desafio.dto.request.VotoDTO;
import com.sicredi.desafio.dto.response.ResultadoVotacaoDTO;
import com.sicredi.desafio.service.PautaVotacaoService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PautaVotacaoController.class)
@ActiveProfiles("test")
public class PautaVotacaoPerformanceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PautaVotacaoService pautaVotacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        Mockito.doNothing().when(pautaVotacaoService).registrarVoto(anyLong(), any(VotoDTO.class));
    }

    @Test
    public void testVotacaoPerformance() throws InterruptedException {
        int numberOfThreads = 100;
        int numberOfRequests = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        Runnable task = getRunnable();

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
    public void testObterResultadoPerformance() throws InterruptedException {
        int numberOfThreads = 100;
        int numberOfRequests = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        when(pautaVotacaoService.obterResultado(anyLong())).thenReturn(new ResultadoVotacaoDTO());

        Runnable task = getObterResultadoRunnable();

        for (int i = 0; i < numberOfRequests; i++) {
            executorService.submit(task);
        }

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(10, TimeUnit.MINUTES);
        if (!terminated) {
            throw new RuntimeException("Timeout: Threads não finalizaram dentro do tempo esperado");
        }
    }

    private Runnable getObterResultadoRunnable() {
        return () -> {
            try {
                mockMvc.perform(get("/votacao/v1/resultado/1"))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private Runnable getRunnable() {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setAssociadoId(1L);
        votoDTO.setPautaOpcaoId(1L);

        return () -> {
            try {
                mockMvc.perform(post("/votacao/v1/votar/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(votoDTO)))
                        .andExpect(status().isCreated());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}