package com.api.muebleria.armadirique.modules.carrito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.muebleria.armadirique.modules.carrito.carritoItem.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {}