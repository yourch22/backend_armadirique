package com.api.muebleria.armadirique.modules.pedido.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class VentaRequestDTO {
    private Long usuarioId;
    private String orderNumber;
    private String direccionEnvio;
    private String metodoPago;
    private List<DetalleVentaDTO> detalles;
}
