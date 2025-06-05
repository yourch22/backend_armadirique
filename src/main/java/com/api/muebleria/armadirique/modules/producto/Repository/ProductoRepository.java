package com.api.muebleria.armadirique.modules.producto.Repository;

import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
