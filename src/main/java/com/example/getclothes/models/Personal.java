package com.example.getclothes.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Builder.Default
    private final UUID id = UUID.randomUUID();
    private String nombre;
    private String apellido;
    private Cargo cargo;
    @ManyToOne
    @JoinColumn
    private Tienda tienda;

    public enum Cargo{
        EMPLEADO, JEFE;
    }


}
