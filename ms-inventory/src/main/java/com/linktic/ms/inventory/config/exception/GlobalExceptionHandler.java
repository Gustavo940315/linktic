package com.linktic.ms.inventory.config.exception;


import com.linktic.ms.inventory.config.ExceptionConfigs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ExceptionConfigs exceptionConfigs;

    public GlobalExceptionHandler(ExceptionConfigs exceptionConfigs) {
        this.exceptionConfigs = exceptionConfigs;
    }

    @ExceptionHandler(MyHandleException.class)
    public ResponseEntity<String> handleCustomException(MyHandleException ex) {
        var msg = exceptionConfigs.getException(ExceptionConfigs.BUSINESS) + " : " + ex.getMessage();
        log.error(msg);
        return ResponseEntity.badRequest().body(msg);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        var msg = exceptionConfigs.getException(ExceptionConfigs.SYSYEM) + " : " + ex.getMessage();
        log.error(msg);
        return ResponseEntity.badRequest().body(msg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> "Validación: %s [%s: %s]".formatted(
                        error.getObjectName(), error.getField(), error.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }
}
