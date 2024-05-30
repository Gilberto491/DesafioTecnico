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
        String motivo = "Argumento do método inválido";
        logger.error(motivo, ex);
        ErrorResponse errorResponse = new ErrorResponse(motivo, HttpStatus.BAD_REQUEST.value());
        ApiResponse<ErrorResponse> response = new ApiResponse<>("Error", errorResponse);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String motivo = "Erro de integridade de dados";
        logger.error(motivo, ex);
        ErrorResponse errorResponse = new ErrorResponse(motivo, HttpStatus.BAD_REQUEST.value());
        ApiResponse<ErrorResponse> response = new ApiResponse<>("Error", errorResponse);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PautaException.class)
    public ResponseEntity<?> handlePautaException(PautaException ex) {
        String motivo = "Erro ao abrir sessão de votação para a pauta";
        logger.error(motivo, ex);
        ErrorResponse errorResponse = new ErrorResponse(motivo, HttpStatus.BAD_REQUEST.value());
        ApiResponse<ErrorResponse> response = new ApiResponse<>("Error", errorResponse);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        String motivo = "Erro geral: " + ex.getMessage();
        logger.error(motivo, ex);
        ErrorResponse errorResponse = new ErrorResponse(motivo, HttpStatus.BAD_REQUEST.value());
        ApiResponse<ErrorResponse> response = new ApiResponse<>("Error", errorResponse);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
