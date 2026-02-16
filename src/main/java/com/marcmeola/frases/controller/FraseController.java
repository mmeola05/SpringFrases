package com.marcmeola.frases.controller;

import com.marcmeola.frases.config.Config;
import com.marcmeola.frases.dto.*;
import com.marcmeola.frases.service.FraseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(Config.API_URL + "/frases")
@RequiredArgsConstructor
@Tag(name = "Frases", description = "Operaciones sobre frases célebres")
public class FraseController {

    private final FraseService fraseService;

    @GetMapping
    @Operation(summary = "Listar frases", description = "Obtiene un listado paginado de frases")
    public ResponseEntity<Page<FraseDTO>> getAll(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(fraseService.findAll(pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar frases", description = "Busca frases que contengan un texto específico")
    public ResponseEntity<List<FraseDTO>> getByTexto(@RequestParam(name = "texto") String texto) {
        return ResponseEntity.ok(fraseService.findByTexto(texto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener frase", description = "Obtiene una frase por su ID")
    public ResponseEntity<FraseDTO> getById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(fraseService.findById(id));
    }

    @GetMapping("/dia")
    @Operation(summary = "Frase del día", description = "Obtiene la frase programada para una fecha específica (por defecto hoy)")
    public ResponseEntity<FraseDTO> getFraseDelDia(
            @RequestParam(required = false, name = "fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (fecha == null) {
            fecha = LocalDate.now();
        }
        return ResponseEntity.ok(fraseService.findFraseDelDia(fecha));
    }

    @PostMapping
    @Operation(summary = "Crear frase", description = "Crea una nueva frase")
    public ResponseEntity<FraseDTO> create(@Valid @RequestBody FraseCreateDTO createDTO) {
        return new ResponseEntity<>(fraseService.create(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar frase", description = "Actualiza una frase existente")
    public ResponseEntity<FraseDTO> update(@PathVariable(name = "id") Long id,
            @Valid @RequestBody FraseCreateDTO createDTO) {
        return ResponseEntity.ok(fraseService.update(id, createDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar frase", description = "Elimina un frase por su ID")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        fraseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/valoraciones")
    @Operation(summary = "Votar frase", description = "Añade un like o dislike a una frase")
    public ResponseEntity<Void> votar(@PathVariable(name = "id") Long id,
            @Valid @RequestBody ValoracionDTO valoracionDTO,
            java.security.Principal principal) {
        fraseService.votar(id, valoracionDTO, principal.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/top")
    @Operation(summary = "Top frases", description = "Obtiene las frases mejor valoradas")
    public ResponseEntity<List<FraseDTO>> getTopFrases(@RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ResponseEntity.ok(fraseService.getTopFrases(limit));
    }
}
