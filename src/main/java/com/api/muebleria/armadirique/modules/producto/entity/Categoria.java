package com.api.muebleria.armadirique.modules.producto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoriaId;

    private String titulo;

    private String descripcion;

    @OneToMany(mappedBy = "categoria",cascade = CascadeType.ALL)

    private Set<Producto> productos = new LinkedHashSet<>();
}