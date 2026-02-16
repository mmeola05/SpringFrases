package com.marcmeola.frases.controller.web;

import com.marcmeola.frases.dto.FraseDTO;
import com.marcmeola.frases.service.FraseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/web/frases")
@RequiredArgsConstructor
public class FraseWebController {

    private final FraseService fraseService;

    @GetMapping("/dia")
    public String verFraseDelDia(Model model) {
        // Obtenemos la frase del día (usando java.time.LocalDate para hoy)
        FraseDTO frase = fraseService.findFraseDelDia(LocalDate.now());

        // La añadimos al modelo para que Thymeleaf pueda acceder a ella
        model.addAttribute("frase", frase);

        // Devolvemos el nombre de la plantilla (sin .html)
        return "frase-del-dia";
    }
}
