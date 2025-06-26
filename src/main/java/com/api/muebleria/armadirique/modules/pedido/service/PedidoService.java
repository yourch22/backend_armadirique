package com.api.muebleria.armadirique.modules.pedido.service;

import com.api.muebleria.armadirique.modules.pedido.dto.VentaRequestDTO;
import com.api.muebleria.armadirique.modules.pedido.dto.VentaResponseDTO;

import java.util.List;

public interface PedidoService {
    void procesarPedido(VentaRequestDTO ventaRequest);

    List<VentaResponseDTO> obtenerPedidosPorUsuario(Long usuarioId);

}
