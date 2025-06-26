package com.api.muebleria.armadirique.modules.pedido.controller;

import com.api.muebleria.armadirique.auth.entity.Usuario;
import com.api.muebleria.armadirique.modules.pedido.dto.VentaRequestDTO;
import com.api.muebleria.armadirique.modules.pedido.dto.VentaResponseDTO;
import com.api.muebleria.armadirique.modules.pedido.entity.Venta;
import com.api.muebleria.armadirique.modules.pedido.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin("*")//dependencia para permitir acceso al controlador
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final UserDetailsService userDetailsService;

    @PostMapping
    public ResponseEntity<Map<String, String>> crearPedido(@RequestBody VentaRequestDTO ventaRequest) {
        pedidoService.procesarPedido(ventaRequest);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Pedido procesado con éxito");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<VentaResponseDTO>> obtenerMisPedidos(Principal principal) {
        Usuario usuario = (Usuario) userDetailsService.loadUserByUsername(principal.getName());

        List<VentaResponseDTO> pedidos = pedidoService.obtenerPedidosPorUsuario(usuario.getUsuarioId());

        return ResponseEntity.ok(pedidos);
    }

}
