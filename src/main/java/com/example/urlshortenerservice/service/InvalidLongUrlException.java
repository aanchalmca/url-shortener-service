package com.example.urlshortenerservice.service;

public class InvalidLongUrlException extends RuntimeException {
    public InvalidLongUrlException() {}

    public InvalidLongUrlException(String message) {
        super(message);
    }
}
