package com.sicredi.desafio.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CPFValidadorService {

    private static final String API_URL = "https://consultacpf-tbc9.onrender.com/api/validate";
    private static final Logger logger = LoggerFactory.getLogger(CPFValidadorService.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public boolean validaCpf(String cpf) {

        String url = API_URL + "/" + cpf;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return casosDeRetorno(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            logger.error("Failed to call API. Status code: {}", e.getStatusCode());
            if (e.getStatusCode().value() == 400) {
                return false;
            }
        } catch (Exception e) {
            logger.error("Ocorreu um erro na chamada de validação do CPF", e);
        }
        return false;
    }

    private boolean casosDeRetorno(String responseBody) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            if (jsonNode.has("Response") && "valid CPF".equals(jsonNode.get("Response").asText())) {
                return true;
            } else if (jsonNode.has("Response") && "Invalid CPF".equals(jsonNode.get("Response").asText())) {
                return false;
            } else if (jsonNode.has("result") && "Error".equals(jsonNode.get("result").asText()) && jsonNode.has("data")) {
                JsonNode dataNode = jsonNode.get("data");
                if (dataNode.has("message") && dataNode.get("message").asText().contains("Invalid CPF")) {
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error("Ocorreu um erro na chamada de validação do CPF", e);
        }
        return false;
    }

}
