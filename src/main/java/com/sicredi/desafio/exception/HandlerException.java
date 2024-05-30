package com.sicredi.desafio.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class HandlerException {

    private static final Logger logger = LoggerFactory.getLogger(HandlerException.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String motivo = "Argumento do método inválido";
        logger.error(motivo);
        return new ResponseEntity<>(
                motivo,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String motivo = "Erro de integridade de dados";
        String message = motivo + e.getMessage();
        logger.error(message, e);
        return new ResponseEntity<>(motivo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PautaException.class)
    public ResponseEntity<String> handlePautaException(PautaException e) {
        logger.error("Erro ao abrir sessão de votação para a pauta: {}", e.getMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        String message = "Erro: " + e.getMessage();
        logger.error(message, e);
        return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }
}
