package org.example.selahvault.service;

import org.example.selahvault.entity.PasswordEntry;
import org.example.selahvault.entity.Usuario;
import org.example.selahvault.repository.PasswordEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PasswordService {

    private final PasswordEntryRepository passwordEntryRepository;
    private final CryptoService cryptoService;

    public PasswordService(PasswordEntryRepository passwordEntryRepository,
                           CryptoService cryptoService) {
        this.passwordEntryRepository = passwordEntryRepository;
        this.cryptoService = cryptoService;
    }

    // Muestra los registros de un usuario con la contrasena lista para verla en pantalla.
    @Transactional(readOnly = true)
    public List<PasswordEntry> listarPorUsuario(Usuario usuario) {
        List<PasswordEntry> registros = passwordEntryRepository.findByUsuario(usuario);
        registros.forEach(this::prepararParaMostrar);
        return registros;
    }

    // Guarda la contrasena cifrada en la base de datos.
    public PasswordEntry guardar(PasswordEntry entry) {
        entry.setContrasena(cryptoService.cifrar(entry.getContrasena()));
        return passwordEntryRepository.save(entry);
    }

    // Busca un registro y valida que pertenezca al usuario conectado.
    @Transactional(readOnly = true)
    public PasswordEntry buscarPorIdYUsuario(Long id, Usuario usuario) {
        PasswordEntry entry = passwordEntryRepository.findByIdAndUsuario(id, usuario).orElse(null);

        if (entry != null) {
            prepararParaMostrar(entry);
        }

        return entry;
    }

    // Elimina un registro del usuario conectado.
    public void eliminar(Long id, Usuario usuario) {
        PasswordEntry entry = passwordEntryRepository.findByIdAndUsuario(id, usuario).orElse(null);

        if (entry != null) {
            passwordEntryRepository.delete(entry);
        }
    }

    // Metodos usados por la API REST.

    // Lista todos los registros de la tabla.
    @Transactional(readOnly = true)
    public List<PasswordEntry> listarTodas() {
        List<PasswordEntry> registros = passwordEntryRepository.findAll();
        registros.forEach(this::prepararParaMostrar);
        return registros;
    }

    // Busca un registro usando el id.
    @Transactional(readOnly = true)
    public Optional<PasswordEntry> buscarPorId(Long id) {
        Optional<PasswordEntry> registro = passwordEntryRepository.findById(id);
        registro.ifPresent(this::prepararParaMostrar);
        return registro;
    }

    // Elimina un registro usando el id.
    public void eliminar(Long id) {
        passwordEntryRepository.deleteById(id);
    }

    // Antes de mostrar una contrasena en pantalla, la descifro.
    private void prepararParaMostrar(PasswordEntry entry) {
        entry.setContrasena(cryptoService.descifrar(entry.getContrasena()));
    }
}
