package org.example.selahvault.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class GeneradorService {

    private final SecureRandom random = new SecureRandom();

    public String generar(int longitud, boolean mayusculas, boolean minusculas, boolean numeros, boolean simbolos) {
        String chars = "";

        if (minusculas) chars += "abcdefghijklmnopqrstuvwxyz";
        if (mayusculas) chars += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (numeros) chars += "0123456789";
        if (simbolos) chars += "!@#$%&*()-_=+";

        if (chars.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }
}