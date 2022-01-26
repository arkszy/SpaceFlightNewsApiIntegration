package com.example.demo.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestException extends Exception {
    private final int statusCode;

    public RestException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
