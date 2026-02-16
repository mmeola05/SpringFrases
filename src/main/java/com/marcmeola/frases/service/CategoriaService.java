package com.marcmeola.frases.service;

import com.marcmeola.frases.dto.*;
import com.marcmeola.frases.model.Categoria;
import com.marcmeola.frases.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public Page<CategoriaDTO> findAll(Pageable pageable) {
        return categoriaRepository.findAll(pageable).map(this::mapToDTO);
    }

    public CategoriaDTO findById(Long id) {
        return categoriaRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + id));
    }

    public CategoriaDTO create(CategoriaCreateDTO createDTO) {
        Categoria categoria = new Categoria();
        categoria.setNombre(createDTO.getNombre());
        return mapToDTO(categoriaRepository.save(categoria));
    }

    public CategoriaDTO update(Long id, CategoriaCreateDTO createDTO) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + id));
        categoria.setNombre(createDTO.getNombre());
        return mapToDTO(categoriaRepository.save(categoria));
    }

    public void delete(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoría no encontrada con id: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO mapToDTO(Categoria categoria) {
        return new CategoriaDTO(categoria.getId(), categoria.getNombre());
    }
}
