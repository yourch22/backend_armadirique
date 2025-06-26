package com.api.muebleria.armadirique.modules.pedido.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleVentaDTO {
    private Long productoId;
    private String nombreProducto;     // ✅ Nombre del producto
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;       // ✅ precioUnitario * cantidad
}
