package com.api.muebleria.armadirique.modules.producto.controller;
import com.api.muebleria.armadirique.modules.producto.dto.CategoriaResponse;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoRequest;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoResponse;
import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import com.api.muebleria.armadirique.modules.producto.service.ICategoriaService;
import com.api.muebleria.armadirique.modules.producto.service.IProductoService;
import com.api.muebleria.armadirique.modules.productoReport.ListarProductoExcel;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page; // <--- ADD THIS LINE
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable; // Ensure this is present
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import static com.google.common.base.Preconditions.*;
import com.google.common.collect.*;
import com.google.common.base.*;

import java.io.IOException;
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
     *Hace la retornacion de la lista de productos
     * @return lista de productos
     */
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    // Nuevo endpoint para obtener productos paginados....
    @GetMapping("/paginado")
    public ResponseEntity<Page<ProductoResponse>> obtenerTodosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre,asc") String[] sort
    ) {
        // Validar tamaño máximo permitido
        if (size > 100) {
            size = 100; // Limitar a 100 resultados por página
        }
        // Separar campo y dirección
        String sortField = sort[0];
        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<ProductoResponse> productosPage = productoService.obtenerTodosPaginado(pageable);
        return ResponseEntity.ok(productosPage);
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

    /**
     * ✅ 2. Filtrado o procesamiento de listas con FluentIterable o Collections2
     * En el endpoint /categorias, podrías aplicar filtros fácilmente con Guava:
     * @return
     */
    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaResponse>> getTypes(){
        List<CategoriaResponse> categoriaResponses = FluentIterable
                .from(categoriaService.listarCategorias())
                .filter(new Predicate<CategoriaResponse>() {
                    public boolean apply(CategoriaResponse c) {
                        return c.getTitulo() != null && !c.getTitulo().isEmpty(); // solo categorías válidas
                    }
                })
                .toList();
        return ResponseEntity.ok(categoriaResponses);
    }



    //reporte excel
    @GetMapping("/exportar/excel")
    public void exportarProductosExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=listado-productos.xlsx");
        List<Producto> productos = productoService.obtenerProductosEntidad();
        ListarProductoExcel exporter = new ListarProductoExcel(productos);
        exporter.export(response);
    }
}
