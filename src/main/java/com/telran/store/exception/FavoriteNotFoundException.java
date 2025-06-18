package com.telran.store.exception;

public class FavoriteNotFoundException extends RuntimeException{

    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
