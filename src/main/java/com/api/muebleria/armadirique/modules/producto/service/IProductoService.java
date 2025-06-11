package com.api.muebleria.armadirique.modules.producto.service;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoRequest;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoResponse;
import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IProductoService {
    ProductoResponse createProduct(ProductoRequest productoRequest);//agregamos nuevo pra guardar img
    List<ProductoResponse> obtenerTodos();
    ProductoResponse obtenerPorId(Long id);
    ProductoResponse updateProduct(Long id,ProductoRequest productoRequest);//otro netodo para actualizar con img
    void eliminar(Long id);

    // Nuevo metodo para obtener productos paginados
    Page<ProductoResponse> obtenerTodosPaginado(Pageable pageable);

    //reporte excel
    List<Producto> obtenerProductosEntidad();

}