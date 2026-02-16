package com.marcmeola.frases.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValoracionDTO {
    @NotNull(message = "Debes indicar si es positiva (true) o negativa (false)")
    private Boolean positiva;
}
