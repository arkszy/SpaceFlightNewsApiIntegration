package com.example.demo.controller;


import com.example.demo.exception.RestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandlingController {

    @ExceptionHandler(RestException.class)
    public ResponseEntity<String> handleRestException(
            RestException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.valueOf(ex.getStatusCode())).body(ex.getMessage());
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<String> handleInterruptedException(
            Exception ex) {
        log.error(ex.getMessage(), ex);
        Thread.currentThread().interrupt();
        return ResponseEntity.internalServerError().body(String.format("something went wrong %s", ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(
            Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(String.format("something went wrong %s", ex));
    }

}
