package com.api.muebleria.armadirique.modules.producto.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * DTO que representa la información necesaria para crear o actualizar un producto.
 */
import jakarta.validation.constraints.*;

@Data
public class ProductoRequest {
    @NotBlank
    private String nombre;

    @NotBlank
    private String descripcion;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal precio;

    @NotNull
    @Min(0)
    private Integer stock;

    private MultipartFile imagenUrl;

    private boolean estado;

    @NotNull
    private Long idCategoria;

    @NotNull
    private Long idUsuario;
}
