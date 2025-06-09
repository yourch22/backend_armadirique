package com.api.muebleria.armadirique.modules.producto.Repository;
import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable; // Ensure this is present

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Spring Data JPA automáticamente generará la implementación para este metodo
    Page<Producto> findAll(Pageable pageable);
    boolean existsByNombreAndCategoria(String nombre, Categoria categoria);
}
