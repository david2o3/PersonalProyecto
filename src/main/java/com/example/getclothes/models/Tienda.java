package com.example.getclothes.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class Tienda {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String ciudad;
    private String direccion;
    private String telefono;

}
