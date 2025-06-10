package com.api.muebleria.armadirique.modules.carrito.carritoItem;

import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import com.api.muebleria.armadirique.auth.entity.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.muebleria.armadirique.modules.carrito.carritoItem.CarritoRepository;
import com.api.muebleria.armadirique.modules.carrito.carritoItem.CarritoItemRepository;
import com.api.muebleria.armadirique.modules.producto.Repository.ProductoRepository;
import com.api.muebleria.armadirique.auth.repository.UsuarioRepository;
import java.util.Optional;
import java.util.List;


@Service
public class CarritoService {
    private final CarritoRepository carritoRepo;
    private final ProductoRepository productoRepo;
    private final CarritoItemRepository itemRepo;
    private final UsuarioRepository usuarioRepo;

    public CarritoService(CarritoRepository carritoRepo, ProductoRepository productoRepo,
                          CarritoItemRepository itemRepo, UsuarioRepository usuarioRepo) {
        this.carritoRepo = carritoRepo;
        this.productoRepo = productoRepo;
        this.itemRepo = itemRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional
    public Carrito obtenerCarritoDeUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = usuario.getCarrito();
        if (carrito == null) {
            throw new RuntimeException("El usuario no tiene carrito asignado");
        }

        return carrito;
    }

    @Transactional
    public Carrito agregarProducto(Long usuarioId, Long productoId, int cantidad) {
        Carrito carrito = obtenerCarritoDeUsuario(usuarioId);
        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Optional<CarritoItem> optionalItem = carrito.getItems().stream()
                .filter(i -> i.getProducto().getProductoId().equals(productoId))
                .findFirst();

        if (optionalItem.isPresent()) {
            CarritoItem item = optionalItem.get();
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            CarritoItem item = new CarritoItem();
            item.setProducto(producto);
            item.setCantidad(cantidad);
            item.setCarrito(carrito);
            carrito.getItems().add(item);
        }

        return carritoRepo.save(carrito);
    }

    @Transactional
    public Carrito vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerCarritoDeUsuario(usuarioId);
        carrito.getItems().forEach(itemRepo::delete);
        carrito.getItems().clear();
        return carritoRepo.save(carrito);
    }

    @Transactional
    public Carrito eliminarProducto(Long usuarioId, Long productoId) {
        Carrito carrito = obtenerCarritoDeUsuario(usuarioId);
        if (carrito == null) {
            throw new RuntimeException("Carrito no encontrado");
        }

        List<CarritoItem> items = carrito.getItems();
        items.removeIf(item -> item.getProducto().getProductoId().equals(productoId));
        carrito.setItems(items);
        return carritoRepo.save(carrito);
    }
    @Transactional
    public Carrito actualizarCantidad(Long usuarioId, Long productoId, int nuevaCantidad) {
        Carrito carrito = obtenerCarritoDeUsuario(usuarioId);
        for (CarritoItem item : carrito.getItems()) {
            if (item.getProducto().getProductoId().equals(productoId)) {
                item.setCantidad(nuevaCantidad);
                carritoRepo.save(carrito);
                break;
            }
        }
        return carrito;
    }
}
