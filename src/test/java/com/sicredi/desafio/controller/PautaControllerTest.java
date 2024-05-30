package com.sicredi.desafio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.desafio.dto.request.AbrirSessaoVotacaoDTO;
import com.sicredi.desafio.dto.request.PautaDTO;
import com.sicredi.desafio.exception.PautaException;
import com.sicredi.desafio.model.Pauta;
import com.sicredi.desafio.service.PautaService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PautaController.class)
@ActiveProfiles("test")
public class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PautaService pautaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCriarPauta() throws Exception {

        PautaDTO pautaDTO = new PautaDTO();
        pautaDTO.setTitulo("Título Teste");
        pautaDTO.setDescricao("Descrição Teste");

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Título Teste");
        pauta.setDescricao("Descrição Teste");

        Mockito.when(pautaService.criarPauta(Mockito.any(PautaDTO.class))).thenReturn(pauta);

        mockMvc.perform(post("/pauta/v1/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pautaDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(pauta)));
    }

    @Test
    public void testAbrirSessaoVotacao() throws Exception {
        Long pautaId = 4L;
        AbrirSessaoVotacaoDTO dto = new AbrirSessaoVotacaoDTO();
        dto.setDuracaoEmMinutos(1L);

        Pauta pauta = new Pauta();
        pauta.setId(pautaId);
        pauta.setTitulo("Título Teste");

        Mockito.when(pautaService.abrirSessaoVotacao(pautaId, dto.getDuracaoEmMinutos())).thenReturn(pauta);

        mockMvc.perform(post("/pauta/v1/abrir-sessao/{pautaId}", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pauta)));
    }

    @Test
    public void testCancelarPauta() throws Exception {
        Long pautaId = 1L;

        Mockito.doNothing().when(pautaService).cancelarPauta(pautaId);

        mockMvc.perform(get("/pauta/v1/cancelar/{pautaId}", pautaId))
                .andExpect(status().isOk());
    }

    @Test
    public void testCriarPautaErro() throws Exception {
        PautaDTO pautaDTO = new PautaDTO();
        pautaDTO.setTitulo("Título Teste");
        pautaDTO.setDescricao("Descrição Teste");

        Mockito.when(pautaService.criarPauta(Mockito.any(PautaDTO.class)))
                .thenThrow(new PautaException("Erro ao criar pauta"));

        mockMvc.perform(post("/pauta/v1/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pautaDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAbrirSessaoVotacaoErro() throws Exception {
        Long pautaId = 1L;
        AbrirSessaoVotacaoDTO dto = new AbrirSessaoVotacaoDTO();
        dto.setDuracaoEmMinutos(10L);

        Mockito.when(pautaService.abrirSessaoVotacao(pautaId, dto.getDuracaoEmMinutos()))
                .thenThrow(new RuntimeException("A pauta já foi iniciada ou está fechada."));

        mockMvc.perform(post("/pauta/v1/abrir-sessao/{pautaId}", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCancelarPautaErro() throws Exception {
        Long pautaId = 1L;

        Mockito.doThrow(new RuntimeException("A pauta não está aberta nem criada."))
                .when(pautaService).cancelarPauta(pautaId);

        mockMvc.perform(get("/pauta/v1/cancelar/{pautaId}", pautaId))
                .andExpect(status().isBadRequest());
    }

}
