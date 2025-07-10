package com.telran.store.exception;

public class OrderAlreadyCanceledException extends RuntimeException {

    public OrderAlreadyCanceledException(String message) {
        super(message);
    }
}
