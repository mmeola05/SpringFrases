package com.marcmeola.frases.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FraseCreateDTO {
    @Schema(description = "Texto de la frase", example = "En un lugar de la Mancha...")
    @NotBlank(message = "El texto no puede estar vacío")
    @Size(min = 5, max = 1000, message = "El texto debe tener entre 5 y 1000 caracteres")
    private String texto;

    @Schema(description = "Fecha programada para ser frase del día", example = "2023-11-20")
    private LocalDate fechaProgramada;

    @Schema(description = "ID del Autor", example = "1")
    @NotNull(message = "El autor es obligatorio")
    private Long autorId;

    @Schema(description = "ID de la Categoría", example = "1")
    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;
}
