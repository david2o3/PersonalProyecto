package com.example.getclothes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) //Esto se pone para que salte la excepcion con el http
public class NotFoundException extends PersonalException{
    public NotFoundException(String mensaje) {
        super(mensaje);
    }
}
