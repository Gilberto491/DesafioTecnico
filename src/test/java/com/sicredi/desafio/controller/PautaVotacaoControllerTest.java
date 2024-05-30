package com.sicredi.desafio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.desafio.dto.request.VotoDTO;
import com.sicredi.desafio.dto.response.ResultadoVotacaoDTO;
import com.sicredi.desafio.service.PautaVotacaoService;
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

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PautaVotacaoController.class)
@ActiveProfiles("test")
public class PautaVotacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PautaVotacaoService pautaVotacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void votar_Sucesso() throws Exception {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setAssociadoId(1L);
        votoDTO.setPautaOpcaoId(1L);

        doNothing().when(pautaVotacaoService).registrarVoto(anyLong(), any(VotoDTO.class));

        mockMvc.perform(post("/votacao/v1/votar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Voto registrado com sucesso"));
    }

    @Test
    public void votar_AssociadoNaoEncontrado() throws Exception {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setAssociadoId(1L);
        votoDTO.setPautaOpcaoId(1L);

        doThrow(new IllegalArgumentException("Associado não encontrado")).when(pautaVotacaoService).registrarVoto(anyLong(), any(VotoDTO.class));

        mockMvc.perform(post("/votacao/v1/votar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void votar_AssociadoJaVotou() throws Exception {
        VotoDTO votoDTO = new VotoDTO();
        votoDTO.setAssociadoId(1L);
        votoDTO.setPautaOpcaoId(1L);

        doThrow(new IllegalArgumentException("Associado já votou nesta pauta")).when(pautaVotacaoService).registrarVoto(anyLong(), any(VotoDTO.class));

        mockMvc.perform(post("/votacao/v1/votar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void obterResultado_Sucesso() throws Exception {
        ResultadoVotacaoDTO resultadoVotacaoDTO = new ResultadoVotacaoDTO();
        resultadoVotacaoDTO.setPautaId(1L);
        resultadoVotacaoDTO.setTituloPauta("Pauta 1");
        HashMap<String, Long> resultados = new HashMap<>();
        resultados.put("Opção 1", 10L);
        resultadoVotacaoDTO.setResultados(resultados);

        Mockito.when(pautaVotacaoService.obterResultado(anyLong())).thenReturn(resultadoVotacaoDTO);

        mockMvc.perform(get("/votacao/v1/resultado/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pautaId").value(1L))
                .andExpect(jsonPath("$.tituloPauta").value("Pauta 1"))
                .andExpect(jsonPath("$.resultados['Opção 1']").value(10L));
    }

    @Test
    public void obterResultado_PautaNaoEncontrada() throws Exception {
        Mockito.when(pautaVotacaoService.obterResultado(anyLong())).thenThrow(new IllegalArgumentException("Pauta não encontrada"));

        mockMvc.perform(get("/votacao/v1/resultado/1"))
                .andExpect(status().isBadRequest());
    }



}
