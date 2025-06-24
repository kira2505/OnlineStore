package com.telran.store.handler;

import com.telran.store.exception.NoSuchCategoryException;
import com.telran.store.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler({NoSuchCategoryException.class, ProductNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(Exception e) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }
}
