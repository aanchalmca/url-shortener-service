package com.example.urlshortenerservice.service;

public class DuplicateLongUrlException extends RuntimeException {
    public DuplicateLongUrlException(){

    }

    public  DuplicateLongUrlException(String message ){
        super(message);
    }

}
