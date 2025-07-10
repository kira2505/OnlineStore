package com.telran.store.handler;

import com.telran.store.exception.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler({NoSuchMethodError.class, ProductNotFoundException.class,
            UserNotFoundException.class, FavoriteNotFoundException.class,
            CartItemNotFoundException.class, CartNotFoundException.class,
            NoSuchCategoryException.class, OrderNotFoundException.class,
            NotFoundProductWithDiscountPrice.class})
    public ResponseEntity<Object> handleNotFoundException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CartBadRequest.class, EmptyCartException.class,
            InsufficientProductQuantityException.class, AmountPaymentExceedsOrderTotalAmount.class,
            OrderAlreadyPaidException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleBadRequest(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({OrderAlreadyCompletedException.class, OrderAlreadyCanceledException.class,
                       FavoriteAlreadyExistsException.class})
    public ResponseEntity<Object> handleConflict(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toUnmodifiableList());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
