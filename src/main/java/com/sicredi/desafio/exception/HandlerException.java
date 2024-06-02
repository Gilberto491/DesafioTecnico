package com.sicredi.desafio.exception;

import com.sicredi.desafio.dto.response.ApiResponse;
import com.sicredi.desafio.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerException {

    private static final Logger logger = LoggerFactory.getLogger(HandlerException.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return buildErrorResponse("Argumento do método inválido", ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return buildErrorResponse("Erro de integridade de dados", ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PautaException.class)
    public ResponseEntity<?> handlePautaException(PautaException ex) {
        return buildErrorResponse("Erro ao abrir sessão de votação para a pauta", ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CPFNaoEncontradoException.class)
    public ResponseEntity<?> handleCpfException(CPFNaoEncontradoException ex) {
        return buildErrorResponse("CPF inválido", ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VotacaoBloqueadaException.class)
    public ResponseEntity<?> handleVotacaoBloqueadaException(VotacaoBloqueadaException ex) {
        return buildErrorResponse("UNABLE_TO_VOTE", ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return buildErrorResponse("Erro geral: " + ex.getMessage(), ex, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> buildErrorResponse(String message, Exception ex, HttpStatus status) {
        logger.error(message, ex);
        ErrorResponse errorResponse = new ErrorResponse(message, status.value());
        ApiResponse<ErrorResponse> response = new ApiResponse<>("Error", errorResponse);
        return new ResponseEntity<>(response, status);
    }
}
