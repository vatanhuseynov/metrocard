package com.ibar.metrocard.exception;

import com.fasterxml.jackson.core.JacksonException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.JDBCException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> responseStatusException(ResponseStatusException ex) {
        log.info("Exception occurred:", ex);
        ErrorMessage errorMessage = new ErrorMessage(ex.getBody().getStatus(), ex.getReason());
        return ResponseEntity.status(ex.getBody().getStatus()).body(errorMessage);
    }


    @ExceptionHandler({HttpServerErrorException.class})
    public ResponseEntity<ErrorMessage> handleHttpServerErrorExceptionInternalServerError(HttpServerErrorException ex) {
        log.error("Exception occurred:", ex);
        String errorText = "Daxili server xətası.";
        ErrorMessage errorMessage = new ErrorMessage(INTERNAL_SERVER_ERROR.value(), errorText);
        return ResponseEntity.internalServerError().body(errorMessage);
    }

    }







