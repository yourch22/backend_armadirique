package com.api.muebleria.armadirique.modules.producto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Builder
@Entity
@Table(name = "productos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productoId;

    private String nombre;
    private String descripcion;

    private BigDecimal precio;

    private Integer stock;

    private String imagenUrl;

    private boolean estado = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id") // recomendable
    private Categoria categoria;
}