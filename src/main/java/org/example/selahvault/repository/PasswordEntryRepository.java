package org.example.selahvault.repository;

import org.example.selahvault.entity.PasswordEntry;
import org.example.selahvault.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Long> {

    List<PasswordEntry> findByUsuario(Usuario usuario);

    Optional<PasswordEntry> findByIdAndUsuario(Long id, Usuario usuario);

}