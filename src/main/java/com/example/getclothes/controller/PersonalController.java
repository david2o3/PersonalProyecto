package com.example.getclothes.controller;

import com.example.getclothes.mapper.PersonalMapper;
import com.example.getclothes.models.Personal;
import com.example.getclothes.models.PersonalDTOCreUpd;
import com.example.getclothes.services.PersonalServiceImpl;
import com.example.getclothes.utils.PaginacionLinks;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/getClothes")
public class PersonalController {
        private final PersonalServiceImpl personalService;
        private final PaginacionLinks paginacionLinks;

        @Autowired
        public PersonalController(PersonalServiceImpl personalService, PaginacionLinks paginacionLinks) {
            this.personalService = personalService;
            this.paginacionLinks = paginacionLinks;
        }

        @GetMapping("/personal")
        public ResponseEntity<Page<Personal>> getAll(
                @RequestParam(required = false) Optional<String> nombre,
                @RequestParam(required = false) Optional<String> apellido,
                @RequestParam(required = false) Optional<Personal.Cargo> cargo,
                @RequestParam(required = false) Optional<String> tienda,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "id") String sortBy, //La manera de como quiero que se vaya mostrando
                @RequestParam(defaultValue = "asc") String direction,
                HttpServletRequest request
        ){
            Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
            Page<Personal> pageResult = personalService.findAll(nombre, apellido, cargo, tienda, PageRequest.of(page, size, sort));
            return ResponseEntity.ok()
                    .header("link", paginacionLinks.createLinkHeader(pageResult, uri))
                    .body(pageResult);
        }

        @GetMapping("/personal/{id}")
        public ResponseEntity<Personal> getId(@PathVariable UUID id){
            return ResponseEntity.ok(personalService.findById(id));
        }

        @PostMapping("/personal")
        public ResponseEntity<PersonalDTOCreUpd> create(@Valid @RequestBody PersonalDTOCreUpd personalDTO){
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    PersonalMapper.convertirPersonalaDTO(
                            personalService.save(personalDTO)));
        }

        @PutMapping("/personal/{id}")
        public ResponseEntity<PersonalDTOCreUpd> update(@PathVariable UUID id, @Valid @RequestBody PersonalDTOCreUpd updatedPersonalDTO){
            return ResponseEntity.ok(
                    PersonalMapper.convertirPersonalaDTO(
                            personalService.update(id, updatedPersonalDTO)));
        }

        @DeleteMapping("/personal/{id}")
        public ResponseEntity<Void> deleteId(@PathVariable UUID id){
            personalService.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public Map<String, String> handleValidationExceptions(
                MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return errors;
        }
    }

