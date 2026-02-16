package com.marcmeola.frases.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutorDTO {
    private Long id;
    private String nombre;
    private Integer anioNacimiento;
    private Integer anioFallecimiento;
    private String profesion;
}
