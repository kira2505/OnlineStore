package com.telran.store.exception;

public class CartBadRequest extends RuntimeException {
    public CartBadRequest(String message) {
        super(message);
    }
}
