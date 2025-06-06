package com.api.muebleria.armadirique.modules.producto.entity;

import com.api.muebleria.armadirique.auth.entity.Usuario;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id") // recomendable
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}