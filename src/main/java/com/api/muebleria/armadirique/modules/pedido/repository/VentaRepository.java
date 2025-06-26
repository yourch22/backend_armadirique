package com.api.muebleria.armadirique.modules.pedido.repository;

import com.api.muebleria.armadirique.modules.pedido.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByUsuarioUsuarioId(Long usuarioId);

}

