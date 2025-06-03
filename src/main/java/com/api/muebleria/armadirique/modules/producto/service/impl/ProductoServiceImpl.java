package com.api.muebleria.armadirique.modules.producto.service.impl;


import com.api.muebleria.armadirique.auth.repository.UsuarioRepository;
import com.api.muebleria.armadirique.modules.producto.Repository.CategoriaRepository;
import com.api.muebleria.armadirique.modules.producto.Repository.ProductoRepository;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoRequest;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoResponse;
import com.api.muebleria.armadirique.modules.producto.entity.Categoria;
import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import com.api.muebleria.armadirique.modules.producto.service.IProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements IProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;


    @Override
    public List<ProductoResponse> obtenerTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return mapToResponse(producto);
    }

    @Override
    public ProductoResponse crear(ProductoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .stock(request.getStock())
                .imagenUrl(request.getImagenUrl())
                //.estado(request.getEstado() != null ? request.getEstado() : true)
                .categoria(categoria)
                .build();

        return mapToResponse(productoRepository.save(producto));
    }

    @Override
    public ProductoResponse actualizar(long id, ProductoRequest request) {
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
        //producto.setEstado(request.getEstado());

        return mapToResponse(productoRepository.save(producto));
    }

    @Override
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
    private ProductoResponse mapToResponse(Producto producto) {
        return ProductoResponse.builder()
                .idProducto(producto.getProductoId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .imagenUrl(producto.getImagenUrl())
                //.estado(producto.getEstado())
                .idCategoria(producto.getCategoria() != null ? producto.getCategoria().getCategoriaId() : null)
                .nombreCategoria(producto.getCategoria() != null ? producto.getCategoria().getTitulo() : null)
                .build();
    }

}
