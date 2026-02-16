package com.marcmeola.frases.controller;

import com.marcmeola.frases.config.Config;
import com.marcmeola.frases.dto.*;
import com.marcmeola.frases.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Config.API_URL + "/autores")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "Operaciones sobre autores")
public class AutorController {

    private final AutorService autorService;
    private final FraseService fraseService;

    @GetMapping
    @Operation(summary = "Listar autores", description = "Obtiene un listado paginado de autores")
    public ResponseEntity<Page<AutorDTO>> getAll(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(autorService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener autor", description = "Obtiene un autor por su ID")
    public ResponseEntity<AutorDTO> getById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(autorService.findById(id));
    }

    @GetMapping("/{id}/frases")
    @Operation(summary = "Listar frases de un autor", description = "Obtiene las frases de un autor")
    public ResponseEntity<List<FraseDTO>> getFrasesByAutor(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(fraseService.findByAutor(id));
    }

    @PostMapping
    @Operation(summary = "Crear autor", description = "Crea un nuevo autor")
    public ResponseEntity<AutorDTO> create(@Valid @RequestBody AutorCreateDTO createDTO) {
        return new ResponseEntity<>(autorService.create(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar autor", description = "Actualiza un autor existente")
    public ResponseEntity<AutorDTO> update(@PathVariable(name = "id") Long id,
            @Valid @RequestBody AutorCreateDTO createDTO) {
        return ResponseEntity.ok(autorService.update(id, createDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar autor", description = "Elimina un autor por su ID")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        autorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
