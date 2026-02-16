package com.marcmeola.frases.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FraseDTO {
    private Long id;
    private String texto;
    private LocalDate fechaProgramada;
    private AutorDTO autor;
    private CategoriaDTO categoria;
    private Long visitas;
}
