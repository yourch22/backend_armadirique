package com.api.muebleria.armadirique.modules.producto.service.impl;
import com.api.muebleria.armadirique.modules.producto.Repository.CategoriaRepository;
import com.api.muebleria.armadirique.modules.producto.dto.CategoriaResponse;
import com.api.muebleria.armadirique.modules.producto.entity.Categoria;
import com.api.muebleria.armadirique.modules.producto.service.ICategoriaService;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl implements ICategoriaService {

    private final CategoriaRepository categoriaRepository;
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }
    @Override
    public List<CategoriaResponse> listarCategorias() {
        List<Categoria> categoriaslist = categoriaRepository.findAll();
        List<CategoriaResponse> categoriasResponse = categoriaslist.stream()
                .map(this::convertToCategoriaResponse)
                .collect(Collectors.toList());
        return categoriasResponse;
    }

    private CategoriaResponse convertToCategoriaResponse(Categoria categoria) {
        return CategoriaResponse.builder()
                .categoriaId(categoria.getCategoriaId())
                .titulo(categoria.getTitulo())
                .descripcion(categoria.getDescripcion())
                .build();
    }
}


