package org.example.selahvault.controller;

import org.example.selahvault.entity.PasswordEntry;
import org.example.selahvault.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/passwords")
public class PasswordRestController {

    @Autowired
    private PasswordService passwordService;

    // Este metodo muestra todos los registros guardados
    @GetMapping
    public List<PasswordEntry> listarTodas() {
        return passwordService.listarTodas();
    }

    // Este metodo busca un registro por su id
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {

        Optional<PasswordEntry> password = passwordService.buscarPorId(id);

        if (password.isPresent()) {

            return ResponseEntity.ok(password.get());

        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontro el registro solicitado");
        }
    }

    // Este metodo permite crear un nuevo registro
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody PasswordEntry entry) {

        try {

            PasswordEntry nuevo = passwordService.guardar(entry);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(nuevo);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo guardar el registro, revisa los datos enviados");
        }
    }

    // Este metodo actualiza un registro que ya existe
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody PasswordEntry datos) {

        Optional<PasswordEntry> existente =
                passwordService.buscarPorId(id);

        if (existente.isPresent()) {

            PasswordEntry password = existente.get();

            password.setTitulo(datos.getTitulo());
            password.setUsuarioSitio(datos.getUsuarioSitio());
            password.setContrasena(datos.getContrasena());
            password.setUrl(datos.getUrl());

            passwordService.guardar(password);

            return ResponseEntity.ok(password);

        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontro el registro solicitado");
        }
    }

    // Este metodo elimina un registro por su id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {

        Optional<PasswordEntry> existente =
                passwordService.buscarPorId(id);

        if (existente.isPresent()) {

            passwordService.eliminar(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();

        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontro el registro solicitado");
        }
    }
}
