package org.example.selahvault.service;

import org.springframework.stereotype.Service;

@Service
public class FortalezaService {

    public String evaluar(String password) {
        int puntos = 0;

        if (password != null && password.length() >= 8) puntos++;
        if (password != null && password.matches(".*[A-Z].*")) puntos++;
        if (password != null && password.matches(".*[a-z].*")) puntos++;
        if (password != null && password.matches(".*\\d.*")) puntos++;
        if (password != null && password.matches(".*[!@#$%&*()\\-_=+].*")) puntos++;

        if (puntos <= 2) return "Débil";
        if (puntos <= 4) return "Media";
        return "Fuerte";
    }
}