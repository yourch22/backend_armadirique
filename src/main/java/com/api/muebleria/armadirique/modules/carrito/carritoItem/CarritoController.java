package com.api.muebleria.armadirique.modules.carrito.carritoItem;

import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import org.springframework.web.bind.annotation.*;
import com.api.muebleria.armadirique.modules.producto.Repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import com.api.muebleria.armadirique.modules.carrito.carritoItem.CarritoService;

import org.springframework.http.ResponseEntity;


import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/carrito")

public class CarritoController {
    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping("/{usuarioId}/agregar/{productoId}")
    public Carrito agregarProducto(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId,
            @RequestParam int cantidad
    ) {
        return carritoService.agregarProducto(usuarioId, productoId, cantidad);
    }
    @DeleteMapping("/{usuarioId}/eliminar/{productoId}")
    public Carrito eliminarProductoDelCarrito(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId) {
        return carritoService.eliminarProducto(usuarioId, productoId);
    }
    @DeleteMapping("/{usuarioId}/vaciar")
    public Carrito vaciarCarrito(@PathVariable Long usuarioId) {
        return carritoService.vaciarCarrito(usuarioId);
    }

    @GetMapping("/{usuarioId}")
    public Carrito obtenerCarrito(@PathVariable Long usuarioId) {
        return carritoService.obtenerCarritoDeUsuario(usuarioId);
    }
    @PutMapping("/{usuarioId}/actualizar/{productoId}")
    public Carrito actualizarCantidad(
            @PathVariable Long usuarioId,
            @PathVariable Long productoId,
            @RequestParam int cantidad
    ) {
        return carritoService.actualizarCantidad(usuarioId, productoId, cantidad);
    }
}

