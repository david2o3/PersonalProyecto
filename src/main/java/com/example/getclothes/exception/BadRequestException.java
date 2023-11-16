package com.example.getclothes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends PersonalException{

    public BadRequestException(String mensaje) {
        super(mensaje);
    }
}
