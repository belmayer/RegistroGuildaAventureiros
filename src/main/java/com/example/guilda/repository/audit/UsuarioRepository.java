package com.example.guilda.repository.audit;

import com.example.guilda.domain.audit.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
