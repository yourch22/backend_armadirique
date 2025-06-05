package com.api.muebleria.armadirique;

import com.api.muebleria.armadirique.auth.entity.Rol;
import com.api.muebleria.armadirique.auth.entity.Usuario;
import com.api.muebleria.armadirique.auth.entity.UsuarioRol;
import com.api.muebleria.armadirique.auth.service.UsuarioService;
import com.api.muebleria.armadirique.excepcions.UsuarioFountException;
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
import java.math.BigDecimal;
import java.util.Optional;

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
			usuario.setApellidos("Flores");
			usuario.setUsername("jorge");
			usuario.setPassword(bCryptPasswordEncoder.encode("12345"));
			usuario.setEmail("jorge@gmail.com");
			usuario.setTelefono("988212020");
			usuario.setPerfil("foto.png");
			usuario.setDireccion("av.arequipa");
			usuario.setEstado(true);
			usuario.setCiudad("Lima");


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

		//CREACION DE CATEGORIA 'MUEBLES' Y 10 PRODUCTOS

		// Crear categoria 
		Optional<Categoria> categoriaOpt = categoriaRepository.findByTitulo("Muebles");

		Categoria categoriaMueble;

		if (categoriaOpt.isPresent()) {
			categoriaMueble = categoriaOpt.get();
		} else {
			categoriaMueble = new Categoria();
			categoriaMueble.setTitulo("Muebles");
			categoriaMueble.setDescripcion("Categoría de productos de mobiliario como sillas, sillones y sofás.");
			categoriaMueble = categoriaRepository.save(categoriaMueble);
		}
				
		// Crear productos
		List<Producto> productos = List.of(
			new Producto(null, "Silla Madera Azul", "Silla de madera sólida pintada en azul, perfecta para comedor moderno.", new BigDecimal("800.00"), 10, "mueble1", true, categoriaMueble, null),
			new Producto(null, "Sillón Rojo", "Sillón tapizado en tela roja vibrante, ideal para salas con estilo.", new BigDecimal("600.00"), 10, "mueble2", true, categoriaMueble, null),
			new Producto(null, "Silla Blanca", "Silla elegante de acabado blanco, ideal para escritorios y tocadores.", new BigDecimal("1000.00"), 10, "mueble3", true, categoriaMueble, null),
			new Producto(null, "Sofá Marrón", "Sofá amplio en tono marrón chocolate, cómodo y acogedor para cualquier sala.", new BigDecimal("1000.00"), 10, "mueble4", true, categoriaMueble, null),
			new Producto(null, "Sillón Plomo", "Sillón individual en tono plomo, diseño moderno y respaldo ergonómico.", new BigDecimal("1000.00"), 10, "mueble5", true, categoriaMueble, null),
			new Producto(null, "Silla Amoblada", "Silla acolchada y forrada con materiales de alta calidad, ideal para oficina.", new BigDecimal("1000.00"), 10, "mueble6", true, categoriaMueble, null),
			new Producto(null, "Sofá Gris", "Sofá contemporáneo gris claro de dos cuerpos, combina con cualquier ambiente.", new BigDecimal("1000.00"), 10, "mueble7", true, categoriaMueble, null),
			new Producto(null, "Silla Verde Giratoria", "Silla de oficina ergonómica con asiento giratorio y color verde lima.", new BigDecimal("1000.00"), 10, "mueble8", true, categoriaMueble, null),
			new Producto(null, "Sofá Azul", "Sofá azul marino de tres cuerpos con cojines suaves y estructura de madera.", new BigDecimal("1000.00"), 10, "mueble9", true, categoriaMueble, null),
			new Producto(null, "Banco Madera", "Banco rústico de madera maciza, ideal para exteriores o cocinas campestres.", new BigDecimal("1000.00"), 10, "mueble10", true, categoriaMueble, null)
		);
		// guardar productos
		for (Producto producto : productos) {
			boolean existe = productoRepository.existsByNombreAndCategoria(producto.getNombre(), categoriaMueble);
			if (!existe) {
				productoRepository.save(producto);
			}
		}
		System.out.println("Productos de prueba creados.");

	}
}
