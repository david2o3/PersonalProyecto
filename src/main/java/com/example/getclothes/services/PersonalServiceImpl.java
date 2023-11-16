package com.example.getclothes.services;

import com.example.getclothes.WebSocket.Notificacion;
import com.example.getclothes.WebSocket.PersonalNotificacion;
import com.example.getclothes.config.WebSocketConfig;
import com.example.getclothes.config.WebSocketHandler;
import com.example.getclothes.exception.BadRequestException;
import com.example.getclothes.exception.NotFoundException;
import com.example.getclothes.mapper.NotificacionMapper;
import com.example.getclothes.mapper.PersonalMapper;
import com.example.getclothes.models.Personal;
import com.example.getclothes.models.PersonalDTOCreUpd;
import com.example.getclothes.models.Tienda;
import com.example.getclothes.repositorys.PersonalRepository;
import com.example.getclothes.repositorys.TiendaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = {"/personal"})
public class PersonalServiceImpl implements PersonalService{

    private final PersonalRepository personalRepository;
    private final PersonalMapper mapper;
    private final TiendaRepository tiendaRepository;

    private final ObjectMapper objectMapper;
    private final WebSocketConfig webSocketConfig;
    private final NotificacionMapper notificacionMapper;
    private WebSocketHandler webSocketPersonalHandler;
    @Autowired
    public PersonalServiceImpl(PersonalRepository personalRepository, PersonalMapper mapper, TiendaRepository tiendaRepository, WebSocketConfig webSocketConfig, NotificacionMapper notificacionMapper) {
        this.personalRepository = personalRepository;
        this.mapper = mapper;
        this.tiendaRepository = tiendaRepository;
        objectMapper = new ObjectMapper();
        this.webSocketConfig = webSocketConfig;
        webSocketPersonalHandler = webSocketConfig.webSocketPersonalHandler();
        this.notificacionMapper = notificacionMapper;
    }

    private Tienda checkTienda(String nombreDireccion) {//Comprueba si existe en la tabla tienda alguna tienda con la ciudad que sea
        var tienda = tiendaRepository.findByDireccionEqualsIgnoreCase(nombreDireccion);
        if (tienda.isEmpty()) {
            throw new BadRequestException("La tienda situada en " + nombreDireccion + " no existe o está borrada");
        }
        return tienda.get();
    }
    @Override
    @CachePut
    public Personal save(PersonalDTOCreUpd personalDTO) {
        var tienda = checkTienda(personalDTO.getTienda());
        Personal personal = mapper.convertirDTOaPersonal(personalDTO, tienda);
        onChange(Notificacion.Tipo.CREATE, personal);
        return personalRepository.save(personal);
    }

    @Override
    @CachePut
    public Personal update(UUID id, PersonalDTOCreUpd personalDTO) {
        var tienda = checkTienda(personalDTO.getTienda());
        Personal existingPersonal = personalRepository.findById(id).orElse(null);
        Personal updatedPersonalDTO = mapper.convertirDTOaPersonal(personalDTO, tienda);

        if (existingPersonal != null) {
            existingPersonal.setNombre(updatedPersonalDTO.getNombre());
            existingPersonal.setApellido(updatedPersonalDTO.getApellido());
            existingPersonal.setCargo(updatedPersonalDTO.getCargo());
            onChange(Notificacion.Tipo.UPDATE, existingPersonal);

            return personalRepository.save(existingPersonal);
        } else {
            throw new NotFoundException("El empleado con id " + id + " no existe");
        }
    }

    @Override
    public Page<Personal> findAll(Optional<String> nombre, Optional<String> apellido, Optional<Personal.Cargo> cargo, Optional<String> tienda, Pageable pageable) {
        Specification<Personal> specNombre = (root, query, criteriaBuilder) ->
                nombre.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> specApellido = (root, query, criteriaBuilder) ->
                apellido.map(a -> criteriaBuilder.like(criteriaBuilder.lower(root.get("apellido")), "%" + a.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> specCargo = (root, query, criteriaBuilder) ->
                cargo.map(c -> criteriaBuilder.like(criteriaBuilder.lower(root.get("cargo")), "%" + c + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        /*Specification<Personal> specTiendaCiudad = (root, query, criteriaBuilder) ->
                tienda.map(tc -> {
                    Join<Personal, Tienda> categoriaJoin = root.join("tienda");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("ciudad")), "%" + tc.toLowerCase() + "%"); // Buscamos por nombre
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));*/

        Specification<Personal> specTiendaDireccion = (root, query, criteriaBuilder) ->
                tienda.map(td -> {
                    Join<Personal, Tienda> categoriaJoin = root.join("tienda");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("direccion")), "%" + td.toLowerCase() + "%"); // Buscamos por nombre
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Personal> criterios = Specification.where(specNombre)
                .and(specApellido).and(specCargo).and(specTiendaDireccion);/*and(specTiendaCiudad).*/
        return personalRepository.findAll(criterios, pageable);
    }


    @Override
    @Cacheable
    public Personal findById(UUID id) {
        Personal existingPersonal = personalRepository.findById(id).orElse(null);
        if (existingPersonal != null){
            return existingPersonal;
        }else {
            throw new NotFoundException("El empleado con id " + id + " no existe");
        }
    }

    @Override
    @CacheEvict
    public void deleteById(UUID id) {
        Personal existingPersonal = personalRepository.findById(id).orElse(null);
        if (existingPersonal != null){
            onChange(Notificacion.Tipo.DELETE, existingPersonal);
            personalRepository.deleteById(id);
        }else {
            throw new NotFoundException("El empleado con id " + id + " no existe");
        }
    }

    void onChange(Notificacion.Tipo tipo, Personal data) {

        if (webSocketPersonalHandler == null) {
            webSocketPersonalHandler = this.webSocketConfig.webSocketPersonalHandler();
        }

        try {
            Notificacion<PersonalNotificacion> notificacion = new Notificacion<>(
                    "Personal",
                    tipo,
                    notificacionMapper.convertirPersonalNotificacion(data),
                    LocalDateTime.now().toString()
            );

            String json = objectMapper.writeValueAsString((notificacion));

            Thread senderThread = new Thread(() -> {
                try {
                    webSocketPersonalHandler.sendMessage(json);
                } catch (Exception e) {
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
