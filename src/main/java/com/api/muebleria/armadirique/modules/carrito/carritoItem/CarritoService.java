package com.api.muebleria.armadirique.modules.carrito.carritoItem;

import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CarritoService {
    private final Map<Long, ItemCarrito> carrito = new HashMap<>();

    public List<ItemCarrito> listarItems() {
        return new ArrayList<>(carrito.values());
    }

    public void agregarProducto(Producto producto, int cantidad) {
        if (carrito.containsKey(producto.getProductoId())) {
            ItemCarrito item = carrito.get(producto.getProductoId());
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            carrito.put(producto.getProductoId(), new ItemCarrito(producto, cantidad));
        }
    }

    public void eliminarProducto(Long productoId) {
        carrito.remove(productoId);
    }

    public void vaciarCarrito() {
        carrito.clear();
    }
}
