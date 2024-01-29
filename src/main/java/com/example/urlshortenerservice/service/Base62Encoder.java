package com.example.urlshortenerservice.service;

import org.springframework.stereotype.Component;

@Component
public class Base62Encoder {

    private final static String BASE_62_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final static int BASE_62 = 62;
    public Base62Encoder(){

    }

    // To encode the sequence to base 62
    public String encode(long number) {

        StringBuilder stringBuilder = new StringBuilder(1);
        do {
            stringBuilder.insert(0, BASE_62_CHARACTERS.charAt((int) (number % BASE_62)));
            number /= BASE_62;
        } while (number > 0);
        while (stringBuilder.length() != 7) {
            stringBuilder.insert(0, '0');
        }
        return stringBuilder.toString();
    }

    // To decode it back
    public long decode(String number) {
        long result = 0L;
        int length = number.length();
        for (int i = 0; i < length; i++) {
            result += (long) Math.pow(BASE_62, i) * BASE_62_CHARACTERS.indexOf(number.charAt(length - i - 1));
        }
        return result;
    }

}
