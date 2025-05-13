package com.api.muebleria.armadirique.auth.repository;

import com.api.muebleria.armadirique.auth.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    //metodo revuelve nombre usuario
    public Usuario findByUsername(String username);
}
