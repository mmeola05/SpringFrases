package com.marcmeola.frases.service;

import com.marcmeola.frases.dto.*;
import com.marcmeola.frases.model.Autor;
import com.marcmeola.frases.repository.AutorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutorService {
    private final AutorRepository autorRepository;

    public Page<AutorDTO> findAll(Pageable pageable) {
        return autorRepository.findAll(pageable).map(this::mapToDTO);
    }

    public AutorDTO findById(Long id) {
        return autorRepository.findById(id).map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Autor no encontrado con id: " + id));
    }

    public AutorDTO create(AutorCreateDTO createDTO) {
        Autor autor = new Autor();
        autor.setNombre(createDTO.getNombre());
        autor.setAnioNacimiento(createDTO.getAnioNacimiento());
        autor.setAnioFallecimiento(createDTO.getAnioFallecimiento());
        autor.setProfesion(createDTO.getProfesion());
        return mapToDTO(autorRepository.save(autor));
    }

    public AutorDTO update(Long id, AutorCreateDTO createDTO) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Autor no encontrado con id: " + id));

        autor.setNombre(createDTO.getNombre());
        autor.setAnioNacimiento(createDTO.getAnioNacimiento());
        autor.setAnioFallecimiento(createDTO.getAnioFallecimiento());
        autor.setProfesion(createDTO.getProfesion());

        return mapToDTO(autorRepository.save(autor));
    }

    public void delete(Long id) {
        if (!autorRepository.existsById(id)) {
            throw new EntityNotFoundException("Autor no encontrado con id: " + id);
        }
        autorRepository.deleteById(id);
    }

    private AutorDTO mapToDTO(Autor autor) {
        return new AutorDTO(autor.getId(), autor.getNombre(), autor.getAnioNacimiento(), autor.getAnioFallecimiento(),
                autor.getProfesion());
    }
}
