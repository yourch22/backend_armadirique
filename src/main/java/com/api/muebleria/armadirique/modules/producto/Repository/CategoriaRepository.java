package com.api.muebleria.armadirique.modules.producto.Repository;
import java.util.Optional;

import com.api.muebleria.armadirique.modules.producto.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByTitulo(String titulo);
}
