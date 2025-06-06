package com.api.muebleria.armadirique.modules.producto.service.impl;

import com.api.muebleria.armadirique.modules.producto.Repository.CategoriaRepository;
import com.api.muebleria.armadirique.modules.producto.Repository.ProductoRepository;
import com.api.muebleria.armadirique.modules.producto.Repository.UsuarioProductRepository;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoRequest;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoResponse;
import com.api.muebleria.armadirique.modules.producto.entity.Categoria;
import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import com.api.muebleria.armadirique.modules.producto.service.IProductoService;
import com.api.muebleria.armadirique.utils.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.Path; // Necesario para FileStorageService

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements IProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioProductRepository usuarioProductRepository;
    private final FileStorageService fileStorageService; // Inyectar el servicio de archivos

    /**
     * Obtiene todos los productos almacenados en la base de datos y los convierte
     * a una lista de objetos {@link ProductoResponse} para su presentación en la capa cliente.
     *
     * @return una lista de respuestas {@link ProductoResponse} que representan todos los productos disponibles.
     */
    @Override
    public List<ProductoResponse> obtenerTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener productos por ID
     * @param id
     * @return
     */
    @Override
    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return mapToResponse(producto);
    }

    @Override
    public ProductoResponse crear(ProductoRequest request, MultipartFile imagenFile) {
        // Obtener categoría o lanzar excepción si no existe
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        String nombreArchivoImagen = null;
        String imagenUrl = request.getImagenUrl(); // Se usa URL enviada en el request si no se sube archivo

        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {
                // Guardar imagen y obtener nombre de archivo
                nombreArchivoImagen = fileStorageService.storeFile(imagenFile, "producto_");

                // Construir URL pública para acceder a la imagen
                imagenUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(nombreArchivoImagen)
                        .toUriString();

            } catch (Exception e) {
                // Mejor lanzar excepción personalizada o manejar con logger
                throw new RuntimeException("Error al guardar la imagen del producto: " + e.getMessage(), e);
            }
        }

        Producto.ProductoBuilder builder = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .stock(request.getStock())
                .imagenUrl(imagenUrl) // URL final de la imagen (archivo o del request)
                .estado(true)
                .categoria(categoria);

        // Si tienes relación con Usuario, asignar aquí
        // builder.usuario(usuario);

        Producto producto = builder.build();
        producto = productoRepository.save(producto);

        return mapToResponse(producto);
/**
 * antes
 Producto.ProductoBuilder builder = Producto.builder();
 builder.nombre(request.getNombre());
 builder.descripcion(request.getDescripcion());
 builder.precio(request.getPrecio());
 builder.stock(request.getStock());
 builder.imagenUrl(request.getImagenUrl());
 builder.estado(true);
 builder.categoria(categoria);
 Producto producto = builder
 .build();
 return mapToResponse(productoRepository.save(producto));
 */

    }

    /**
     * Metodo ctualizar productos
     * @param id
     * @param request
     * @return
     */
    @Override
    public ProductoResponse actualizar(long id, ProductoRequest request, MultipartFile imagenFile) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setCategoria(categoria);
        producto.setEstado(request.isEstado());

        return mapToResponse(productoRepository.save(producto));
    }

    /**
     *  Metodo elimina Productos por ID
     * @param id
     */
    @Override
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    /**
     * Convierte una entidad Producto a un objeto de respuesta ProductoResponse,
     * extrayendo los datos relevantes incluyendo los datos asociados como categoría y usuario.
     *
     * @param producto la entidad Producto a convertir
     * @return una instancia de ProductoResponse con los datos del producto formateados para ser enviados como respuesta
     */
    private ProductoResponse mapToResponse(Producto producto) {
        return ProductoResponse.builder()
                .idProducto(producto.getProductoId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .imagenUrl(producto.getImagenUrl())
                .estado(producto.isEstado())
                .idCategoria(producto.getCategoria() != null ? producto.getCategoria().getCategoriaId() : null)
                .nombreCategoria(producto.getCategoria() != null ? producto.getCategoria().getTitulo() : null)
                .idUsuario(producto.getUsuario() != null ? producto.getUsuario().getUsuarioId() : null)
                .nombreUsuario(producto.getUsuario() != null ? producto.getUsuario().getNombre() : null)
                .build();
    }

}
