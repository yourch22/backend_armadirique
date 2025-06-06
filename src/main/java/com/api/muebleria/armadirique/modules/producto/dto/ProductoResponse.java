package com.api.muebleria.armadirique.modules.producto.dto;

import com.api.muebleria.armadirique.auth.entity.Usuario;
import com.api.muebleria.armadirique.modules.producto.entity.Categoria;
import lombok.*;

import java.math.BigDecimal;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {
    private Long idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String imagenUrl;
    private boolean estado;
    private Long idCategoria;
    private String nombreCategoria;
    private Long idUsuario;
    private String nombreUsuario;
}
