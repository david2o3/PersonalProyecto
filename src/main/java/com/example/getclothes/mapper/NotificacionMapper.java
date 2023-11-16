package com.example.getclothes.mapper;

import com.example.getclothes.WebSocket.PersonalNotificacion;
import com.example.getclothes.models.Personal;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {
    public PersonalNotificacion convertirPersonalNotificacion(Personal personal){
        return new PersonalNotificacion(
                personal.getId(), personal.getNombre(),personal.getApellido(), personal.getCargo(), personal.getTienda().getDireccion()
        );
    }
}