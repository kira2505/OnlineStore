package com.telran.store.exception;

public class InsufficientProductQuantityException extends  RuntimeException {

    public InsufficientProductQuantityException(String message) {
        super(message);
    }
}
