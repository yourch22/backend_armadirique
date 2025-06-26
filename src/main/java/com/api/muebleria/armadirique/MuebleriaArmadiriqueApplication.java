package com.api.muebleria.armadirique;

import com.api.muebleria.armadirique.auth.entity.Rol;
import com.api.muebleria.armadirique.auth.entity.Usuario;
import com.api.muebleria.armadirique.auth.entity.UsuarioRol;
import com.api.muebleria.armadirique.auth.service.UsuarioService;
import com.api.muebleria.armadirique.excepcions.UsuarioFountException;
import com.api.muebleria.armadirique.modules.carrito.entity.Carrito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;


import com.api.muebleria.armadirique.modules.producto.entity.Producto;
import com.api.muebleria.armadirique.modules.producto.service.impl.ProductoServiceImpl;
import com.api.muebleria.armadirique.modules.producto.entity.Categoria;
import com.api.muebleria.armadirique.modules.producto.Repository.CategoriaRepository;
import com.api.muebleria.armadirique.modules.producto.Repository.ProductoRepository;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Optional;

//import com.api.muebleria.armadirique.modules.carrito.carritoItem.Carrito;

// Archivo principal de la aplicación
@SpringBootApplication
public class MuebleriaArmadiriqueApplication implements CommandLineRunner {//implementacion solo para probar ingresar datos

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ProductoRepository productoRepository;

	public static void main(String[] args) {
		SpringApplication.run(MuebleriaArmadiriqueApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {

		try{
		Usuario usuario = new Usuario();
			usuario.setNombre("Jorge");
			usuario.setApellidos("Flores M");
			usuario.setUsername("admin");
			usuario.setPassword(bCryptPasswordEncoder.encode("12345"));
			usuario.setEmail("admin@gmail.com");
			usuario.setTelefono("988212020");
			usuario.setPerfil("perfil/perfil_1_icon_user.png");
			usuario.setDireccion("av.arequipa");
			usuario.setEstado(true);
			usuario.setCiudad("Lima");
			//Carrito de usuario
			/*Carrito carrito = new Carrito();
			carrito.setUsuario(usuario);
			carrito.setItems(new ArrayList<>());
			usuario.setCarrito(carrito);
*/
			Rol rol = new Rol();
			rol.setRolId(1L);
			rol.setRolNombre("ADMIN");
			Set<UsuarioRol> usuariosRoles = new HashSet<>();
			UsuarioRol usuarioRol = new UsuarioRol();
			usuarioRol.setRol(rol);
			usuarioRol.setUsuario(usuario);
			usuariosRoles.add(usuarioRol);
			Usuario usuarioGuardado = usuarioService.guardarUsuario(usuario,usuariosRoles);
			System.out.println(usuarioGuardado.getUsername());
		}catch (UsuarioFountException exception){
			exception.printStackTrace();
		}
		try {
			// --- Nuevo Usuario CLIENTE ---
			Usuario usuarioCliente = new Usuario();
			usuarioCliente.setNombre("Mario"); // Nombre diferente
			usuarioCliente.setApellidos("Gomez R");
			usuarioCliente.setUsername("cliente"); // Username diferente
			usuarioCliente.setPassword(bCryptPasswordEncoder.encode("12345")); // Contraseña diferente
			usuarioCliente.setEmail("cliente@gmail.com"); // Email diferente
			usuarioCliente.setTelefono("987654321");
			usuarioCliente.setPerfil("perfil/perfil_2_icon_user.png"); // Perfil diferente (si tienes)
			usuarioCliente.setDireccion("calle del sol 456");
			usuarioCliente.setEstado(true);
			usuarioCliente.setCiudad("Cusco");
			//Carrito de usuario
			/*Carrito carrito2 = new Carrito();
			carrito2.setUsuario(usuarioCliente);
			carrito2.setItems(new ArrayList<>());
			usuarioCliente.setCarrito(carrito2);*/

			Rol rolCliente = new Rol();
			rolCliente.setRolId(2L); // <-- ¡IMPORTANTE! Asegúrate que este ID exista en tu tabla de Roles como "CLIENT"
			rolCliente.setRolNombre("CLIENTE"); // El nombre del rol en tu base de datos

			Set<UsuarioRol> usuariosRolesCliente = new HashSet<>();
			UsuarioRol usuarioRolCliente = new UsuarioRol();
			usuarioRolCliente.setRol(rolCliente);
			usuarioRolCliente.setUsuario(usuarioCliente);
			usuariosRolesCliente.add(usuarioRolCliente);

			Usuario usuarioGuardadoCliente = usuarioService.guardarUsuario(usuarioCliente, usuariosRolesCliente);
			System.out.println("Usuario CLIENTE guardado/existente: " + usuarioGuardadoCliente.getUsername());

		} catch (UsuarioFountException exception){
			System.out.println("Error: El usuario CLIENTE ya existe. " + exception.getMessage());
			// exception.printStackTrace(); // Descomenta si necesitas el stack trace completo
		}

		//CREACION DE CATEGORIA 'MUEBLES' Y 10 PRODUCTOS

		//#################################### Crear categorias ########################################
		// Crear categoria muebles
		Categoria categoriaMuebles;
		Optional<Categoria> categoriaMueblesOpt = categoriaRepository.findByTitulo("Muebles");

		if (categoriaMueblesOpt.isPresent()) {
			categoriaMuebles = categoriaMueblesOpt.get();
		} else {
			categoriaMuebles = new Categoria();
			categoriaMuebles.setTitulo("Muebles");
			categoriaMuebles.setDescripcion("Categoría de productos de mobiliario como sillones y sofás.");
			categoriaMuebles = categoriaRepository.save(categoriaMuebles);
		}
		// Crear categoria sillas
		Categoria categoriaSillas;
		Optional<Categoria> categoriaSillasOpt = categoriaRepository.findByTitulo("Sillas");

		if (categoriaSillasOpt.isPresent()) {
			categoriaSillas = categoriaSillasOpt.get();
		} else {
			categoriaSillas = new Categoria();
			categoriaSillas.setTitulo("Sillas");
			categoriaSillas.setDescripcion("Categoría de productos tipo sillas.");
			categoriaSillas = categoriaRepository.save(categoriaSillas);
		}
		// Crear categoria banco
		Categoria categoriaBancos;
		Optional<Categoria> categoriaBancosOpt = categoriaRepository.findByTitulo("Bancos");

		if (categoriaBancosOpt.isPresent()) {
			categoriaBancos = categoriaBancosOpt.get();
		} else {
			categoriaBancos = new Categoria();
			categoriaBancos.setTitulo("Bancos");
			categoriaBancos.setDescripcion("Categoría de bancos elegantes.");
			categoriaBancos = categoriaRepository.save(categoriaBancos);
		}

		//############################# Crear productos ####################################
		List<Producto> productosGenerados = List.of(
			//new Producto(idProducto,Nombre,Descripcion,Precio,Stock,imagenUrl,Estado,Categoria,Usuario)
			new Producto(null, "Silla Terciopelo", "Silla de terciopelo suave, perfecta para comedor moderno.", new BigDecimal("800.00"), 10, "products/SillaTerciopelo.jpg", true, categoriaSillas, null),
			new Producto(null, "Silla Moderna", "Silla tapizada en tela, ideal para salas con estilo.", new BigDecimal("600.00"), 10, "products/SillaModerna.jpg", true, categoriaSillas, null),
			new Producto(null, "Silla Recolchada", "Silla elegante de acabado recolchado, ideal para escritorios y tocadores.", new BigDecimal("1000.00"), 10, "products/SillaRecolchada.jpg", true, categoriaSillas, null),
			new Producto(null, "Sofá Puff Marron", "Sofá Puff, cómodo y acogedor para cualquier sala.", new BigDecimal("1000.00"), 10, "products/SofaPuff.jpg", true, categoriaMuebles, null),
			new Producto(null, "Sillón Plomo", "Sillón individual en tono plomo, diseño moderno y respaldo ergonómico.", new BigDecimal("300.00"), 10, "products/SillonPlomo.jpg", true, categoriaMuebles, null),
			new Producto(null, "Banco Amoblado", "Banco acolchado y forrado con materiales de alta calidad, ideal para oficina.", new BigDecimal("200.00"), 10, "products/BancoAmoblado.jpg", true, categoriaBancos, null),
			new Producto(null, "Sofá Clasico", "Sofá antiguo, combina con cualquier ambiente.", new BigDecimal("500.00"), 10, "products/SofaClasico.jpg", true, categoriaMuebles, null),
			new Producto(null, "Silla Giratoria", "Silla de oficina ergonómica con asiento giratorio.", new BigDecimal("200.00"), 10, "products/SillaGiratoria.jpg", true, categoriaMuebles, null),
			new Producto(null, "Sofá Refinado", "Sofá con cojines suaves y estructura de madera.", new BigDecimal("600.00"), 10, "products/SofaRefinado.jpg", true, categoriaMuebles, null),
			new Producto(null, "Banco Madera", "Banco rústico de madera maciza, ideal para exteriores o cocinas campestres.", new BigDecimal("100.00"), 10, "products/BancoMadera.jpg", true, categoriaBancos, null)
		);
		// guardar productos
		for (Producto producto : productosGenerados) {
			boolean existe = productoRepository.existsByNombreAndCategoria(producto.getNombre(), producto.getCategoria());
			if (!existe) {
				productoRepository.save(producto);
			}
		}
		System.out.println("Productos de prueba creados.");

	}
}
