package org.example.selahvault.controller;

import org.example.selahvault.entity.PasswordEntry;
import org.example.selahvault.entity.Usuario;
import org.example.selahvault.service.FortalezaService;
import org.example.selahvault.service.GeneradorService;
import org.example.selahvault.service.PasswordService;
import org.example.selahvault.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/passwords")
public class PasswordController {

    private final PasswordService passwordService;
    private final UsuarioService usuarioService;
    private final GeneradorService generadorService;
    private final FortalezaService fortalezaService;

    public PasswordController(PasswordService passwordService,
                              UsuarioService usuarioService,
                              GeneradorService generadorService,
                              FortalezaService fortalezaService) {
        this.passwordService = passwordService;
        this.usuarioService = usuarioService;
        this.generadorService = generadorService;
        this.fortalezaService = fortalezaService;
    }

    private Usuario obtenerUsuario(Authentication auth) {
        return usuarioService.buscarPorUsername(auth.getName());
    }

    @GetMapping
    public String listar(Model model, Authentication auth) {
        Usuario usuario = obtenerUsuario(auth);
        model.addAttribute("entries", passwordService.listarPorUsuario(usuario));
        model.addAttribute("passwordGenerada", null);
        model.addAttribute("fortaleza", null);
        return "dashboard";
    }

    @GetMapping("/new")
    public String nuevoForm(Model model) {
        model.addAttribute("entry", new PasswordEntry());
        return "form-password";
    }

    @PostMapping("/save")
    public String guardar(@Valid @ModelAttribute("entry") PasswordEntry entry,
                          BindingResult result,
                          Authentication auth) {
        if (result.hasErrors()) {
            return "form-password";
        }

        Usuario usuario = obtenerUsuario(auth);
        entry.setUsuario(usuario);
        passwordService.guardar(entry);
        return "redirect:/passwords";
    }

    @GetMapping("/edit/{id}")
    public String editarForm(@PathVariable Long id, Authentication auth, Model model) {
        Usuario usuario = obtenerUsuario(auth);
        PasswordEntry entry = passwordService.buscarPorIdYUsuario(id, usuario);

        if (entry == null) {
            return "redirect:/passwords";
        }

        model.addAttribute("entry", entry);
        return "form-password";
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable Long id, Authentication auth) {
        Usuario usuario = obtenerUsuario(auth);
        passwordService.eliminar(id, usuario);
        return "redirect:/passwords";
    }

    @PostMapping("/generate")
    public String generar(@RequestParam(defaultValue = "12") int longitud,
                          @RequestParam(defaultValue = "false") boolean mayusculas,
                          @RequestParam(defaultValue = "false") boolean minusculas,
                          @RequestParam(defaultValue = "false") boolean numeros,
                          @RequestParam(defaultValue = "false") boolean simbolos,
                          Model model,
                          Authentication auth) {

        Usuario usuario = obtenerUsuario(auth);
        model.addAttribute("entries", passwordService.listarPorUsuario(usuario));
        model.addAttribute("passwordGenerada",
                generadorService.generar(longitud, mayusculas, minusculas, numeros, simbolos));
        model.addAttribute("fortaleza", null);
        return "dashboard";
    }

    @PostMapping("/evaluate")
    public String evaluar(@RequestParam String password, Model model, Authentication auth) {
        Usuario usuario = obtenerUsuario(auth);
        model.addAttribute("entries", passwordService.listarPorUsuario(usuario));
        model.addAttribute("fortaleza", fortalezaService.evaluar(password));
        model.addAttribute("passwordGenerada", null);
        return "dashboard";
    }
}