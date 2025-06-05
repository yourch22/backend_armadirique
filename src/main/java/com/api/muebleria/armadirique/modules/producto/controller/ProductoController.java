package com.api.muebleria.armadirique.modules.producto.controller;

import com.api.muebleria.armadirique.modules.producto.dto.CategoriaResponse;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoRequest;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoResponse;
import com.api.muebleria.armadirique.modules.producto.service.ICategoriaService;
import com.api.muebleria.armadirique.modules.producto.service.IProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/productos")
@CrossOrigin("*")//dependencia para permitir acceso al controlador
public class ProductoController {

    private final IProductoService productoService;
    private final ICategoriaService categoriaService;

    public ProductoController(IProductoService productoService, ICategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }
    /**
     * Obtiene todos los productos disponibles.
     *
     * @return lista de productos
     */
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    /**
     * Obtiene un producto por su ID.
     *
     * @param id identificador del producto
     * @return producto encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    /**
     * Crea un nuevo producto.
     *
     * @param request datos del nuevo producto
     * @return producto creado
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProductoResponse> crear(
            @RequestPart("producto") ProductoRequest request, // "producto" es el nombre de la parte que contiene el JSON
            @RequestPart(value = "imagenFile", required = false) MultipartFile imagenFile) { // "imagenFile" es el nombre de la parte que contiene el archivo) {
        try {
            ProductoResponse productoCreado = productoService.crear(request, imagenFile);
            return new ResponseEntity<>(productoCreado, HttpStatus.CREATED);
        } catch (Exception e) {
            // Considera un manejo de errores más específico y devolver un HttpStatus adecuado
            // Por ejemplo, si la categoría no existe, podría ser un BAD_REQUEST
            // Si hay un error al guardar el archivo, podría ser un INTERNAL_SERVER_ERROR
            // e.printStackTrace(); // Para depuración, pero usa un logger en producción
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Ejemplo genérico
        }
    }

    /**
     * Actualiza un producto existente.
     *
     * @param id identificador del producto a actualizar
     * @param request datos actualizados del producto
     * @return producto actualizado
     */
    @PutMapping(value = "/{id}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @RequestPart("producto") ProductoRequest request,
            @RequestPart(value = "imagenFile", required = false) MultipartFile imagenFile) {
        try {
            ProductoResponse productoActualizado = productoService.actualizar(id, request, imagenFile);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) { // Por ejemplo, ProductoNotFoundException
            // e.printStackTrace();
            if (e.getMessage().contains("Producto no encontrado") || e.getMessage().contains("Categoría no encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // O BAD_REQUEST si es error de validación
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Ejemplo genérico
        }
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id identificador del producto a eliminar
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaResponse>> getTypes(){
        List<CategoriaResponse> categoriaResponses = categoriaService.listarCategorias();
        return new ResponseEntity<>(categoriaResponses, HttpStatus.OK);
    }
}
