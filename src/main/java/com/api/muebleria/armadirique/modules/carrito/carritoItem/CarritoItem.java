package com.api.muebleria.armadirique.modules.carrito.carritoItem;
// CarritoItem.java
import jakarta.persistence.*;
import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import com.api.muebleria.armadirique.modules.carrito.carritoItem.Carrito;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CarritoItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Producto producto;

    private Integer cantidad;

    @ManyToOne
    @JsonIgnore
    private Carrito carrito;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Carrito getCarrito() { return carrito; }
    public void setCarrito(Carrito carrito) { this.carrito = carrito; }
}
