package org.example.selahvault.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class CryptoService {

    private static final String PREFIJO = "ENC:";

    // Clave simple para la practica. Debe tener 16 caracteres para AES.
    private static final String CLAVE = "SelahVaultKey202";

    public String cifrar(String texto) {
        if (texto == null || texto.isBlank()) {
            return texto;
        }

        // Si ya esta cifrada, no la vuelvo a cifrar.
        if (texto.startsWith(PREFIJO)) {
            return texto;
        }

        try {
            SecretKeySpec key = new SecretKeySpec(CLAVE.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] textoCifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
            return PREFIJO + Base64.getEncoder().encodeToString(textoCifrado);

        } catch (Exception e) {
            throw new RuntimeException("No se pudo cifrar la contrasena");
        }
    }

    public String descifrar(String textoCifrado) {
        if (textoCifrado == null || textoCifrado.isBlank()) {
            return textoCifrado;
        }

        // Si no tiene el prefijo, significa que todavia no esta cifrada.
        if (!textoCifrado.startsWith(PREFIJO)) {
            return textoCifrado;
        }

        try {
            String textoSinPrefijo = textoCifrado.replace(PREFIJO, "");

            SecretKeySpec key = new SecretKeySpec(CLAVE.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] textoOriginal = cipher.doFinal(Base64.getDecoder().decode(textoSinPrefijo));
            return new String(textoOriginal, StandardCharsets.UTF_8);

        } catch (Exception e) {
            return "No se pudo mostrar";
        }
    }
}
