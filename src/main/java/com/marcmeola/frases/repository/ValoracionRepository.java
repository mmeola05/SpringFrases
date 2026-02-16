package com.marcmeola.frases.repository;

import com.marcmeola.frases.model.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {

    Optional<Valoracion> findByFraseIdAndUsuarioId(Long fraseId, Long usuarioId);

    Long countByFraseIdAndPositiva(Long fraseId, boolean positiva);
}
