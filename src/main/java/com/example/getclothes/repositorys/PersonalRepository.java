package com.example.getclothes.repositorys;

import com.example.getclothes.models.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Creamos el repositorio extendéndolo de JPA, siguiendo DAO
 * Con ello ya tenemos las operaciones básicas de CRUD y Paginación
 * extiende de JpaSpecificationExecutor para tener las opciones de Specificación y busqueda con Criteria
 */
@Repository
public interface PersonalRepository extends JpaRepository<Personal, UUID>, JpaSpecificationExecutor<Personal> {

}