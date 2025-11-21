package com.mercadolibre.examen_mercadolibre.exception;

import com.mercadolibre.examen_mercadolibre.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(InvalidDnaSequenceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDnaSequenceException(
            InvalidDnaSequenceException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                "Bad Request - DNA Validation Error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Maneja cualquier otra RuntimeException no capturada, devolviendo 500 Internal Server Error.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleGenericRuntimeException(
            RuntimeException ex, WebRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                "Internal Server Error",
                "Ha ocurrido un error inesperado en el servidor.",
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}