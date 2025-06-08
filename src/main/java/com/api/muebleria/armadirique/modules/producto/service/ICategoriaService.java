package com.api.muebleria.armadirique.modules.producto.service;

import com.api.muebleria.armadirique.modules.producto.dto.CategoriaResponse;
import com.api.muebleria.armadirique.modules.producto.entity.Categoria;

import java.util.List;

public interface ICategoriaService {
    List<CategoriaResponse> listarCategorias();
}
