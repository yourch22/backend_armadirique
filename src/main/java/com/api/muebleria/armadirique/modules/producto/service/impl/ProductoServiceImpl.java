package com.api.muebleria.armadirique.modules.producto.service.impl;
import com.api.muebleria.armadirique.auth.entity.Usuario;
import com.api.muebleria.armadirique.auth.repository.UsuarioRepository;
import com.api.muebleria.armadirique.modules.producto.Repository.CategoriaRepository;
import com.api.muebleria.armadirique.modules.producto.Repository.ProductoRepository;
import com.api.muebleria.armadirique.modules.producto.Repository.UsuarioProductRepository;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoRequest;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoResponse;
import com.api.muebleria.armadirique.modules.producto.entity.Categoria;
import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import com.api.muebleria.armadirique.modules.producto.service.IProductoService;
import com.api.muebleria.armadirique.utils.FileUploadUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductoServiceImpl implements IProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final FileUploadUtil fileUploadUtil;
    @Value("${upload.dir.base:uploads}")
    private String baseUploadDir;

    public static final String PRODUCT_SUBFOLDER = "products"; // Define subfolder for categories

    public ProductoServiceImpl(ProductoRepository productoRepository, UsuarioProductRepository usuarioProductRepository,
                               CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository, FileUploadUtil fileUploadUtil) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;// Asigna al campo
        this.fileUploadUtil = fileUploadUtil;
    }

    @Override
    @Transactional
    public ProductoResponse createProduct(ProductoRequest productoRequest) {
        Producto producto = new Producto();
        producto.setNombre(productoRequest.getNombre());
        producto.setDescripcion(productoRequest.getDescripcion());
        producto.setPrecio(productoRequest.getPrecio());
        producto.setStock(productoRequest.getStock());
        MultipartFile imagenFile = productoRequest.getImagenUrl(); // Ahora accedes a 'imagenFile'
        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {
                // Llama al método NO ESTÁTICO del bean inyectado:
                String fileName = fileUploadUtil.saveFile(baseUploadDir, PRODUCT_SUBFOLDER, imagenFile);
                //fileUploadUtil.saveFile(null, PRODUCT_SUBFOLDER, imagenFile);
                producto.setImagenUrl(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file: " + e.getMessage());
            }
        }
        producto.setEstado(productoRequest.isEstado());
        // 1. Obtener la Categoria existente por su ID
        Categoria categoria = categoriaRepository.findById(productoRequest.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + productoRequest.getIdCategoria()));
        producto.setCategoria(categoria); // Asigna la categoría obtenida (persistente)
        // 2. Obtener el Usuario existente por su ID
        Usuario usuario = usuarioRepository.findById(productoRequest.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + productoRequest.getIdUsuario()));
        producto.setUsuario(usuario); // Asigna el usuario obtenido (persistente)
        Producto savedProducto = productoRepository.save(producto);
        return mapToResponse(savedProducto);

    }

    @Override
    public List<ProductoResponse> obtenerTodos() {
        return productoRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
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
    @Transactional
    public ProductoResponse updateProduct(Long id, ProductoRequest productoRequest) {
        return productoRepository.findById(id).map(existingProducto -> {
            existingProducto.setNombre(productoRequest.getNombre());
            existingProducto.setDescripcion(productoRequest.getDescripcion());
            existingProducto.setPrecio(productoRequest.getPrecio());
            existingProducto.setStock(productoRequest.getStock());
            if (productoRequest.getImagenUrl() != null && !productoRequest.getImagenUrl().isEmpty()) {
                try {
                    // Delete old photo if exists
                    if (existingProducto.getImagenUrl() != null) {
                        FileUploadUtil.deleteFile(baseUploadDir, existingProducto.getImagenUrl());
                    }
                    String fileName = fileUploadUtil.saveFile(baseUploadDir, PRODUCT_SUBFOLDER, productoRequest.getImagenUrl());
                    existingProducto.setImagenUrl(fileName);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to update file: " + e.getMessage());
                }
            } else if (productoRequest.getImagenUrl() == null) {
                if (existingProducto.getImagenUrl() != null) {
                    try {
                        FileUploadUtil.deleteFile(baseUploadDir, existingProducto.getImagenUrl());
                        existingProducto.setImagenUrl(null); // Clear path in DB
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to delete old file: " + e.getMessage());
                    }
                }
            }
            existingProducto.setEstado(productoRequest.isEstado());
            existingProducto.setCategoria(categoriaRepository.findById(productoRequest.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + productoRequest.getIdCategoria())));
            existingProducto.setUsuario(usuarioRepository.findById(productoRequest.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + productoRequest.getIdUsuario())));
            Producto updatedProducto = productoRepository.save(existingProducto);
            return mapToResponse(updatedProducto);
        }).orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }
    /**
     *  Metodo elimina Productos por ID
     * @param id
     */
    @Override
    public void eliminar(Long id) {
        Optional<Producto> productOptional = productoRepository.findById(id);
        if (productOptional.isPresent()) {
            Producto producto = productOptional.get();
            if (producto.getImagenUrl() != null) {
                try {
                    FileUploadUtil.deleteFile(baseUploadDir, producto.getImagenUrl());
                } catch (IOException e) {
                    // Log the error but proceed with deleting the category record
                    System.err.println("Could not delete file for category " + id + ": " + e.getMessage());
                }
            }
            productoRepository.delete(producto);
        } else {
            throw new RuntimeException("Category not found with id: " + id);
        }
    }

    // Implementación del metodo de paginación
    @Override
    public Page<ProductoResponse> obtenerTodosPaginado(Pageable pageable) {
        return productoRepository.findAll(pageable).map(this::mapToResponse); // Mapea cada Producto a ProductoResponse
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
