package com.example.getclothes.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

    @Data
    public class PersonalDTOCreUpd {
        @NotBlank(message = "El nombre no puede estar vacio")
        @Size(min = 2, max = 30, message = "El nombre tiene que tener entre 2 y 30 caracteres")
        String nombre;

        @NotBlank(message = "El apellido no puede estar vacio")
        @Size(min = 2, max = 30, message = "El apellido tiene que tener entre 2 y 30 caracteres")
        String apellido;
        @NotNull(message = "El cargo no puede estar vacío")
        Personal.Cargo cargo;

        String tienda;
    }

