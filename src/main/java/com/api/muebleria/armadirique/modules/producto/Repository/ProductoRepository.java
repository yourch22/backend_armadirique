package com.api.muebleria.armadirique.modules.producto.Repository;

import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.muebleria.armadirique.modules.producto.entity.Categoria;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByNombreAndCategoria(String nombre, Categoria categoria);
}
