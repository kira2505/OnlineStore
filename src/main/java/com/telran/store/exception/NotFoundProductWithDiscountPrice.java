package com.telran.store.exception;

public class NotFoundProductWithDiscountPrice extends RuntimeException {
    public NotFoundProductWithDiscountPrice(String message) {
        super(message);
    }
}
