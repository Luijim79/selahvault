package org.example.selahvault.controller;

import org.example.selahvault.service.UsuarioService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam @NotBlank String username,
                           @RequestParam @NotBlank String password,
                           Model model) {

        if (usuarioService.existeUsuario(username)) {
            model.addAttribute("error", "El usuario ya existe");
            return "register";
        }

        usuarioService.registrarUsuario(username, password);
        model.addAttribute("success", "Usuario registrado correctamente");
        return "login";
    }
}