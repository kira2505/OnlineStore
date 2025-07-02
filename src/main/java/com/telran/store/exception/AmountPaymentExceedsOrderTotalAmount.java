package com.telran.store.exception;

public class AmountPaymentExceedsOrderTotalAmount extends RuntimeException {
    public AmountPaymentExceedsOrderTotalAmount(String message) {
        super(message);
    }
}
