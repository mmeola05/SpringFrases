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
@RequestMapping(Config.API_URL + "/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Operaciones sobre categorías")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final FraseService fraseService;

    @GetMapping
    @Operation(summary = "Listar categorías", description = "Obtiene un listado paginado de categorías")
    public ResponseEntity<Page<CategoriaDTO>> getAll(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(categoriaService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría", description = "Obtiene una categoría por su ID")
    public ResponseEntity<CategoriaDTO> getById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(categoriaService.findById(id));
    }

    @GetMapping("/{id}/frases")
    @Operation(summary = "Listar frases de una categoría", description = "Obtiene las frases de una categoría")
    public ResponseEntity<List<FraseDTO>> getFrasesByCategoria(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(fraseService.findByCategoria(id));
    }

    @PostMapping
    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría")
    public ResponseEntity<CategoriaDTO> create(@Valid @RequestBody CategoriaCreateDTO createDTO) {
        return new ResponseEntity<>(categoriaService.create(createDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", description = "Actualiza una categoría existente")
    public ResponseEntity<CategoriaDTO> update(@PathVariable(name = "id") Long id,
            @Valid @RequestBody CategoriaCreateDTO createDTO) {
        return ResponseEntity.ok(categoriaService.update(id, createDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría por su ID")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
