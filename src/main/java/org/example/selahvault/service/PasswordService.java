package org.example.selahvault.service;

import org.example.selahvault.entity.PasswordEntry;
import org.example.selahvault.entity.Usuario;
import org.example.selahvault.repository.PasswordEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {

    private final PasswordEntryRepository passwordEntryRepository;

    public PasswordService(PasswordEntryRepository passwordEntryRepository) {
        this.passwordEntryRepository = passwordEntryRepository;
    }

    public List<PasswordEntry> listarPorUsuario(Usuario usuario) {
        return passwordEntryRepository.findByUsuario(usuario);
    }

    public void guardar(PasswordEntry entry) {
        passwordEntryRepository.save(entry);
    }

    public PasswordEntry buscarPorIdYUsuario(Long id, Usuario usuario) {
        return passwordEntryRepository.findByIdAndUsuario(id, usuario).orElse(null);
    }

    public void eliminar(Long id, Usuario usuario) {
        PasswordEntry entry = buscarPorIdYUsuario(id, usuario);
        if (entry != null) {
            passwordEntryRepository.delete(entry);
        }
    }
}