package com.api.muebleria.armadirique.modules.pedido.service.impl;

import com.api.muebleria.armadirique.auth.entity.Usuario;
import com.api.muebleria.armadirique.auth.repository.UsuarioRepository;
import com.api.muebleria.armadirique.modules.pedido.dto.VentaRequestDTO;
import com.api.muebleria.armadirique.modules.pedido.dto.DetalleVentaDTO;
import com.api.muebleria.armadirique.modules.pedido.dto.VentaResponseDTO;
import com.api.muebleria.armadirique.modules.pedido.entity.DetalleVenta;
import com.api.muebleria.armadirique.modules.pedido.entity.Venta;
import com.api.muebleria.armadirique.modules.pedido.repository.DetalleVentaRepository;
import com.api.muebleria.armadirique.modules.pedido.repository.VentaRepository;
import com.api.muebleria.armadirique.modules.pedido.service.PedidoService;
import com.api.muebleria.armadirique.modules.producto.Repository.ProductoRepository;
import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;



import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public void procesarPedido(VentaRequestDTO ventaRequest) {
        Usuario usuario = usuarioRepository.findById(ventaRequest.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Venta venta = Venta.builder()
                .usuario(usuario)
                .fecha(LocalDateTime.now())
                .direccionEnvio(ventaRequest.getDireccionEnvio())
                .metodoPago(ventaRequest.getMetodoPago())
                .estado("PENDIENTE")
                .orderNumber(ventaRequest.getOrderNumber())
                .build();

        List<DetalleVenta> detalles = ventaRequest.getDetalles().stream().map(det -> {
            Producto producto = productoRepository.findById(det.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            BigDecimal subtotal = det.getPrecioUnitario().multiply(BigDecimal.valueOf(det.getCantidad()));

            // Descontar stock
            producto.setStock(producto.getStock() - det.getCantidad());
            productoRepository.save(producto);

            return DetalleVenta.builder()
                    .producto(producto)
                    .cantidad(det.getCantidad())
                    .precioUnitario(det.getPrecioUnitario())
                    .subtotal(subtotal)
                    .venta(venta)
                    .build();
        }).toList();

        venta.setDetalles(detalles);
        venta.setTotal(detalles.stream()
                .map(DetalleVenta::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        ventaRepository.save(venta);
    }
    private VentaResponseDTO mapToDTO(Venta venta) {
        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setVentaId(venta.getVentaId());
        dto.setOrderNumber(venta.getOrderNumber());
        dto.setFecha(venta.getFecha());
        dto.setTotal(venta.getTotal());
        dto.setEstado(venta.getEstado());
        dto.setMetodoPago(venta.getMetodoPago());
        dto.setDireccionEnvio(venta.getDireccionEnvio());

        List<DetalleVentaDTO> detallesDTO = venta.getDetalles().stream().map(detalle -> {
            DetalleVentaDTO d = new DetalleVentaDTO();
            d.setCantidad(detalle.getCantidad());
            d.setPrecioUnitario(detalle.getPrecioUnitario());
            d.setSubtotal(detalle.getSubtotal());
            d.setProductoId(detalle.getProducto().getProductoId()); // Opcional
            d.setNombreProducto(
                    detalle.getProducto() != null ? detalle.getProducto().getNombre() : "Producto eliminado"
            );
            return d;
        }).toList();

        dto.setDetalles(detallesDTO);
        return dto;
    }

    @Override
    public List<VentaResponseDTO> obtenerPedidosPorUsuario(Long usuarioId) {
        List<Venta> ventas = ventaRepository.findByUsuarioUsuarioId(usuarioId);
        return ventas.stream()
                .map(this::mapToDTO)
                .toList();
    }

}
