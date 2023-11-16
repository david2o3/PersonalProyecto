package com.example.getclothes.services;
import com.example.getclothes.models.Personal;
import com.example.getclothes.models.PersonalDTOCreUpd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface PersonalService {
        Personal save(PersonalDTOCreUpd personalDTO);
        Personal update(UUID id, PersonalDTOCreUpd personalDTO);
        Page<Personal> findAll(Optional<String> nombre, Optional<String> apellido, Optional<Personal.Cargo> cargo,Optional<String> tienda, Pageable pageable);
        Personal findById(UUID id);
        void deleteById(UUID id);
    }
