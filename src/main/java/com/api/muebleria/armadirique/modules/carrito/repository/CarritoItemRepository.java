package com.api.muebleria.armadirique.modules.carrito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.muebleria.armadirique.modules.carrito.entity.CarritoItem;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {}