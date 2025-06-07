package com.api.muebleria.armadirique.modules.producto.controller;
import com.api.muebleria.armadirique.modules.producto.dto.CategoriaResponse;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoRequest;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoResponse;
import com.api.muebleria.armadirique.modules.producto.service.ICategoriaService;
import com.api.muebleria.armadirique.modules.producto.service.IProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // ¡IMPORTANTE! Indica que esperas multipart/form-data para que permita seleccionar imagenes
    public ResponseEntity<ProductoResponse> createProduct(
            @ModelAttribute ProductoRequest request, BindingResult result) {
        if (result.hasErrors()) {
            // manejar errores de validación
            return ResponseEntity.badRequest().build();
        }
        ProductoResponse response = productoService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
     * Actualiza un producto existente.
     *
     * @param id identificador del producto a actualizar
     * @param request datos actualizados del producto
     * @return producto actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductoRequest request,
            BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            ProductoResponse updatedProduct = productoService.updateProduct(id, request);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
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
