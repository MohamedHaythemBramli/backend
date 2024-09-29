package com.example.backend.controller;

import com.example.backend.dto.WebResponse;
import com.example.backend.utilities.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import jakarta.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<WebResponse<Object>> handle(IllegalArgumentException e) {
        return ResponseUtils.error400Response(e.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<WebResponse<Object>> handle(NoResourceFoundException e) {
        return ResponseUtils.error404Response(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<Object>> handle(ConstraintViolationException e) {
        return ResponseUtils.errorValidationResponse(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<Object>> handle(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return ResponseUtils.error500Response(e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<WebResponse<Object>> handle(MissingServletRequestParameterException e) {
        return ResponseUtils.error400Response(e.getMessage());
    }
}
