package com.api.muebleria.armadirique.modules.producto.service;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoRequest;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductoService {
    List<ProductoResponse> obtenerTodos();
    ProductoResponse obtenerPorId(Long id);
    ProductoResponse crear(ProductoRequest request, MultipartFile imagenFile);
    ProductoResponse actualizar(long id, ProductoRequest request, MultipartFile imagenFile);
    void eliminar(Long id);
}