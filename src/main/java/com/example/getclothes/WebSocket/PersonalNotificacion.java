package com.example.getclothes.WebSocket;

import com.example.getclothes.models.Personal;
import com.example.getclothes.models.Tienda;

import java.util.UUID;

public record PersonalNotificacion(
        UUID id,
        String nombre,
        String apellido,
        Personal.Cargo cargo,
        String tienda
){

}
