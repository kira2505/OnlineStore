package com.telran.store.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException (String message) {
        super(message);
    }
}
