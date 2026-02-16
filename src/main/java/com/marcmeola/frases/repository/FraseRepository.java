package com.marcmeola.frases.repository;

import com.marcmeola.frases.model.Frase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FraseRepository extends JpaRepository<Frase, Long> {

    Optional<Frase> findByFechaProgramada(LocalDate fecha);

    List<Frase> findByAutorId(Long autorId);

    List<Frase> findByTextoContainingIgnoreCase(String texto);

    List<Frase> findByCategoriaId(Long categoriaId);

    @Query("SELECT v.frase FROM Valoracion v WHERE v.positiva = true GROUP BY v.frase ORDER BY COUNT(v) DESC")
    List<Frase> findTopFrases(org.springframework.data.domain.Pageable pageable);
}
