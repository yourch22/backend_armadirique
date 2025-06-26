package com.api.muebleria.armadirique.modules.pedido.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaResponseDTO {
    private Long ventaId;
    private String orderNumber;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado;
    private String metodoPago;
    private String direccionEnvio;
    private List<DetalleVentaDTO> detalles;
}
