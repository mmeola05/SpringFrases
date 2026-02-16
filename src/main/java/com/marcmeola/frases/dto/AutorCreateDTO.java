package com.marcmeola.frases.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutorCreateDTO {
    @Schema(description = "Nombre del autor", example = "Miguel de Cervantes")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Schema(description = "Año de nacimiento del autor", example = "1547")
    @NotNull(message = "El año de nacimiento es obligatorio")
    @Min(value = -5000, message = "Año no válido")
    @Max(value = 3000, message = "Año no válido")
    private Integer anioNacimiento;

    @Schema(description = "Año de fallecimiento del autor", example = "1616")
    @Min(value = -5000, message = "Año no válido")
    @Max(value = 3000, message = "Año no válido")
    private Integer anioFallecimiento;

    @Schema(description = "Profesión principal", example = "Escritor")
    private String profesion;
}
