package com.api.muebleria.armadirique.modules.carrito.carritoItem;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.api.muebleria.armadirique.modules.producto.entity.Producto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCarrito {
    private Producto producto;
    private int cantidad;

}
