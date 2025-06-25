package com.telran.store.handler;

import com.telran.store.exception.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler({NoSuchMethodError.class, ProductNotFoundException.class,
            UserNotFoundException.class, FavoriteNotFoundException.class,
            CartItemNotFoundException.class, CartNotFoundException.class,
            CartBadRequest.class})
    public ResponseEntity<Object> handleNotFoundException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
