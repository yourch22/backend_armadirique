package com.api.muebleria.armadirique.modules.producto.Repository;

import com.api.muebleria.armadirique.auth.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioProductRepository extends JpaRepository<Usuario, Long> {
}
