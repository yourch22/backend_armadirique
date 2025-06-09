package com.api.muebleria.armadirique.modules.carrito.carritoItem;

import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public List<ItemCarrito> verCarrito() {
        return carritoService.listarItems();
    }

    @PostMapping("/agregar")
    public void agregarProducto(@RequestBody Producto producto, @RequestParam int cantidad) {
        carritoService.agregarProducto(producto, cantidad);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        carritoService.eliminarProducto(id);
    }

    @DeleteMapping("/vaciar")
    public void vaciarCarrito() {
        carritoService.vaciarCarrito();
    }
}
