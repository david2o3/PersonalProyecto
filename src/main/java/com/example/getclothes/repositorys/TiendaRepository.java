package com.example.getclothes.repositorys;

import com.example.getclothes.models.Personal;
import com.example.getclothes.models.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TiendaRepository extends JpaRepository <Tienda, UUID>, JpaSpecificationExecutor<Tienda> {
    Optional<Tienda> findByDireccionEqualsIgnoreCase(String direccion);
}
