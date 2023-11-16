package com.example.getclothes.mapper;

import com.example.getclothes.models.Personal;
import com.example.getclothes.models.PersonalDTOCreUpd;
import com.example.getclothes.models.Tienda;
import org.springframework.stereotype.Component;

@Component
public class PersonalMapper {
    public static Personal convertirDTOaPersonal(PersonalDTOCreUpd dto, Tienda tienda){
        Personal personal = Personal.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .cargo(dto.getCargo())
                .tienda(tienda)

                .build();

        return personal;
    }

    //Un DTO es lo que puede ver el usuario (lo que está en la interfaz)
    public static PersonalDTOCreUpd convertirPersonalaDTO(Personal personal) {
        PersonalDTOCreUpd dto = new PersonalDTOCreUpd();
        dto.setNombre(personal.getNombre());
        dto.setApellido(personal.getNombre());
        dto.setCargo(personal.getCargo());
        dto.setTienda(personal.getTienda().getCiudad());
        dto.setTienda(personal.getTienda().getDireccion());
        return dto;
    }

}

