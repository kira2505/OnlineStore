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
            NoSuchCategoryException.class, OrderNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CartBadRequest.class, EmptyCartException.class,
            InsufficientProductQuantityException.class, AmountPaymentExceedsOrderTotalAmount.class, OrderAlreadyPaidException.class})
    public ResponseEntity<Object> handleBadRequest(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderAlreadyCompletedException.class)
    public ResponseEntity<Object> handleConflict(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
}
