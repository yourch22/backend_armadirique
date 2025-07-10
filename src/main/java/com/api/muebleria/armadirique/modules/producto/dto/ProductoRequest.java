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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public MultipartFile getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(MultipartFile imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
