package com.marcmeola.frases.service;

import com.marcmeola.frases.dto.*;
import com.marcmeola.frases.model.*;
import com.marcmeola.frases.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FraseService {
    private final FraseRepository fraseRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final ValoracionRepository valoracionRepository;
    private final UsuarioRepository usuarioRepository;

    public Page<FraseDTO> findAll(Pageable pageable) {
        return fraseRepository.findAll(pageable).map(this::mapToDTO);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<FraseDTO> findByTexto(String texto) {
        return fraseRepository.findByTextoContainingIgnoreCase(texto).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @org.springframework.transaction.annotation.Transactional
    public FraseDTO findById(Long id) {
        Frase frase = fraseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Frase no encontrada con id: " + id));

        // Incrementar visitas
        if (frase.getVisitas() == null) {
            frase.setVisitas(1L);
        } else {
            frase.setVisitas(frase.getVisitas() + 1);
        }
        fraseRepository.save(frase);

        return mapToDTO(frase);
    }

    public FraseDTO findFraseDelDia(LocalDate fecha) {
        return fraseRepository.findByFechaProgramada(fecha).map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("No hay frase programada para la fecha: " + fecha));
    }

    public List<FraseDTO> findByAutor(Long autorId) {
        return fraseRepository.findByAutorId(autorId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<FraseDTO> findByCategoria(Long categoriaId) {
        return fraseRepository.findByCategoriaId(categoriaId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public FraseDTO create(FraseCreateDTO createDTO) {
        Autor autor = autorRepository.findById(createDTO.getAutorId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Autor no encontrado con id: " + createDTO.getAutorId()));

        Categoria categoria = categoriaRepository.findById(createDTO.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoría no encontrada con id: " + createDTO.getCategoriaId()));

        Frase frase = new Frase();
        frase.setTexto(createDTO.getTexto());
        frase.setFechaProgramada(createDTO.getFechaProgramada());
        frase.setAutor(autor);
        frase.setCategoria(categoria);

        return mapToDTO(fraseRepository.save(frase));
    }

    public FraseDTO update(Long id, FraseCreateDTO createDTO) {
        Frase frase = fraseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Frase no encontrada con id: " + id));

        Autor autor = autorRepository.findById(createDTO.getAutorId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Autor no encontrado con id: " + createDTO.getAutorId()));

        Categoria categoria = categoriaRepository.findById(createDTO.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoría no encontrada con id: " + createDTO.getCategoriaId()));

        frase.setTexto(createDTO.getTexto());
        frase.setFechaProgramada(createDTO.getFechaProgramada());
        frase.setAutor(autor);
        frase.setCategoria(categoria);

        return mapToDTO(fraseRepository.save(frase));
    }

    public void delete(Long id) {
        if (!fraseRepository.existsById(id)) {
            throw new EntityNotFoundException("Frase no encontrada con id: " + id);
        }
        fraseRepository.deleteById(id);
    }

    public void votar(Long fraseId, ValoracionDTO valoracionDTO, String username) {
        Frase frase = fraseRepository.findById(fraseId)
                .orElseThrow(() -> new EntityNotFoundException("Frase no encontrada con id: " + fraseId));

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + username));

        Valoracion valoracion = valoracionRepository.findByFraseIdAndUsuarioId(fraseId, usuario.getId())
                .orElse(new Valoracion());

        if (valoracion.getId() == null) {
            valoracion.setFrase(frase);
            valoracion.setUsuario(usuario);
        }

        valoracion.setPositiva(valoracionDTO.getPositiva());
        valoracionRepository.save(valoracion);
    }

    public List<FraseDTO> getTopFrases(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return fraseRepository.findTopFrases(pageable).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private FraseDTO mapToDTO(Frase frase) {
        FraseDTO dto = new FraseDTO();
        dto.setId(frase.getId());
        dto.setTexto(frase.getTexto());
        dto.setFechaProgramada(frase.getFechaProgramada());
        dto.setVisitas(frase.getVisitas());

        AutorDTO autorDto = new AutorDTO(
                frase.getAutor().getId(),
                frase.getAutor().getNombre(),
                frase.getAutor().getAnioNacimiento(),
                frase.getAutor().getAnioFallecimiento(),
                frase.getAutor().getProfesion());
        dto.setAutor(autorDto);

        CategoriaDTO catDto = new CategoriaDTO(
                frase.getCategoria().getId(),
                frase.getCategoria().getNombre());
        dto.setCategoria(catDto);

        return dto;
    }
}
