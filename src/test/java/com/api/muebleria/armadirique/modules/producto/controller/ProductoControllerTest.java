package com.api.muebleria.armadirique.modules.producto.controller;

import com.api.muebleria.armadirique.modules.producto.dto.CategoriaResponse;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoRequest;
import com.api.muebleria.armadirique.modules.producto.dto.ProductoResponse;
import com.api.muebleria.armadirique.modules.producto.service.ICategoriaService;
import com.api.muebleria.armadirique.modules.producto.service.IProductoService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;
import java.util.Objects; // Importar Objects para usar Objects.requireNonNull

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para ProductoController.
 * Utiliza Mockito para simular las dependencias y JUnit 5 para la ejecución de las pruebas.
 */
class ProductoControllerTest {

    // Instancia del controlador que se va a probar, donde se inyectarán los mocks.
    @InjectMocks
    private ProductoController productoController;

    // Mock de la interfaz de servicio de productos.
    @Mock
    private IProductoService productoService;

    // Mock de la interfaz de servicio de categorías.
    @Mock
    private ICategoriaService categoriaService;

    // Mock de BindingResult para simular errores de validación.
    @Mock
    private BindingResult bindingResult;

    // Objeto para manejar la apertura y cierre de los mocks de Mockito.
    private AutoCloseable mocks;

    /**
     * Configuración inicial antes de cada prueba.
     * Se encarga de inicializar los mocks y de inyectarlos en la instancia del controlador.
     */
    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    /**
     * Limpieza después de cada prueba.
     * Cierra los mocks para liberar recursos y evitar posibles interacciones entre pruebas.
     */
    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    /**
     * Prueba para la creación exitosa de un producto.
     * Verifica que el controlador devuelva un estado HTTP 201 y el producto creado.
     */
    @Test
    void createProduct() {
        // Prepara una solicitud de producto y una respuesta esperada.
        ProductoRequest request = new ProductoRequest();
        ProductoResponse expectedResponse = new ProductoResponse();
        expectedResponse.setIdProducto(1L); // Asignar un ID para hacerla más representativa

        // Configura el mock de BindingResult para indicar que no hay errores de validación.
        when(bindingResult.hasErrors()).thenReturn(false);
        // Configura el mock de productoService para devolver la respuesta esperada al crear un producto.
        when(productoService.createProduct(request)).thenReturn(expectedResponse);

        // Llama al método del controlador que se está probando.
        ResponseEntity<ProductoResponse> response = productoController.createProduct(request, bindingResult);

        // Verifica que el código de estado HTTP sea 201 (Created).
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        // Verifica que el cuerpo de la respuesta sea el producto esperado.
        assertEquals(expectedResponse, response.getBody());
        // Verifica que el método createProduct del servicio haya sido llamado exactamente una vez.
        verify(productoService, times(1)).createProduct(request);
    }

    /**
     * Prueba para la creación de un producto con una solicitud inválida.
     * Verifica que el controlador devuelva un estado HTTP 400.
     */
    @Test
    void createProduct_invalidRequest() {
        // Configura el mock de BindingResult para indicar que hay errores de validación.
        when(bindingResult.hasErrors()).thenReturn(true);

        // Llama al método del controlador con una solicitud (que no importa su contenido aquí, ya que BindingResult simula el error).
        ResponseEntity<ProductoResponse> response = productoController.createProduct(new ProductoRequest(), bindingResult);

        // Verifica que el código de estado HTTP sea 400 (Bad Request).
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // Verifica que el cuerpo de la respuesta sea nulo, ya que no se procesa el producto.
        assertNull(response.getBody());
        // Verifica que el método createProduct del servicio NUNCA haya sido llamado.
        verify(productoService, never()).createProduct(any(ProductoRequest.class));
    }

    /**
     * Prueba para la obtención de todos los productos.
     * Verifica que el controlador devuelva un estado HTTP 200 y la lista de productos.
     */
    @Test
    void obtenerTodos() {
        // Prepara una lista simulada de productos.
        List<ProductoResponse> mockList = List.of(new ProductoResponse(), new ProductoResponse());
        // Configura el mock de productoService para devolver la lista simulada.
        when(productoService.obtenerTodos()).thenReturn(mockList);

        // Llama al método del controlador.
        ResponseEntity<List<ProductoResponse>> response = productoController.obtenerTodos();

        // Verifica que el código de estado HTTP sea 200 (OK).
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Verifica que el tamaño de la lista en el cuerpo de la respuesta sea el esperado.
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        // Verifica que el método obtenerTodos del servicio haya sido llamado exactamente una vez.
        verify(productoService, times(1)).obtenerTodos();
    }

    /**
     * Prueba para la obtención paginada de productos.
     * Verifica que el controlador devuelva un estado HTTP 200 y una página de productos.
     */
    @Test
    void obtenerTodosPaginado() {
        // Prepara una página simulada de productos.
        Page<ProductoResponse> mockPage = new PageImpl<>(List.of(new ProductoResponse()));
        // Configura el mock de productoService para devolver la página simulada para cualquier Pageable.
        when(productoService.obtenerTodosPaginado(any(Pageable.class))).thenReturn(mockPage);

        // Llama al método del controlador con parámetros de paginación y ordenamiento.
        ResponseEntity<Page<ProductoResponse>> response = productoController.obtenerTodosPaginado(0, 10, new String[]{"nombre", "asc"});

        // Verifica que el código de estado HTTP sea 200 (OK).
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Verifica que el número total de elementos en la página sea el esperado.
        assertEquals(1, Objects.requireNonNull(response.getBody()).getTotalElements());
        // Verifica que el método obtenerTodosPaginado del servicio haya sido llamado exactamente una vez con cualquier Pageable.
        verify(productoService, times(1)).obtenerTodosPaginado(any(Pageable.class));
    }

    /**
     * Prueba para la obtención de un producto por su ID.
     * Verifica que el controlador devuelva un estado HTTP 200 y el producto encontrado.
     */
    @Test
    void obtenerPorId() {
        // Define un ID de prueba.
        Long id = 1L;
        // Prepara una respuesta de producto esperada.
        ProductoResponse expectedResponse = new ProductoResponse();
        expectedResponse.setIdProducto(id);
        // Configura el mock de productoService para devolver la respuesta esperada para el ID.
        when(productoService.obtenerPorId(id)).thenReturn(expectedResponse);

        // Llama al método del controlador.
        ResponseEntity<ProductoResponse> response = productoController.obtenerPorId(id);

        // Verifica que el código de estado HTTP sea 200 (OK).
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Verifica que el cuerpo de la respuesta sea el producto esperado.
        assertEquals(expectedResponse, response.getBody());
        // Verifica que el método obtenerPorId del servicio haya sido llamado exactamente una vez con el ID correcto.
        verify(productoService, times(1)).obtenerPorId(id);
    }

    /**
     * Prueba para la actualización exitosa de un producto.
     * Verifica que el controlador devuelva un estado HTTP 200 y el producto actualizado.
     */
    @Test
    void updateProduct() {
        // Define un ID de prueba.
        Long id = 1L;
        // Prepara una solicitud de producto para la actualización y una respuesta esperada.
        ProductoRequest request = new ProductoRequest();
        ProductoResponse expectedResponse = new ProductoResponse();
        expectedResponse.setIdProducto(id); // Asegurar que la respuesta tenga el ID

        // Configura el mock de BindingResult para indicar que no hay errores de validación.
        when(bindingResult.hasErrors()).thenReturn(false);
        // Configura el mock de productoService para devolver la respuesta esperada al actualizar un producto.
        when(productoService.updateProduct(id, request)).thenReturn(expectedResponse);

        // Llama al método del controlador.
        ResponseEntity<?> response = productoController.updateProduct(id, request, bindingResult);

        // Verifica que el código de estado HTTP sea 200 (OK).
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Verifica que el cuerpo de la respuesta sea el producto esperado.
        assertEquals(expectedResponse, response.getBody());
        // Verifica que el método updateProduct del servicio haya sido llamado exactamente una vez.
        verify(productoService, times(1)).updateProduct(id, request);
    }

    /**
     * Prueba para la actualización de un producto con una solicitud inválida.
     * Verifica que el controlador devuelva un estado HTTP 400.
     */
    @Test
    void updateProduct_invalidRequest() {
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = productoController.updateProduct(1L, new ProductoRequest(), bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // Cambia esta línea:
        // assertNull(response.getBody());
        // Por: Afirmar que es un mapa vacío o un objeto vacío si conoces su tipo
        assertTrue(response.getBody() instanceof Map); // Verifica si es un mapa
        assertTrue(((Map<?, ?>) response.getBody()).isEmpty()); // Verifica si el mapa está vacío

        // Si quieres ser muy específico sobre un objeto JSON vacío:
        // assertEquals("{}", response.getBody().toString()); // Menos robusto, depende de toString()

        verify(productoService, never()).updateProduct(anyLong(), any(ProductoRequest.class));
    }

    /**
     * Prueba para la actualización de un producto que no se encuentra.
     * Verifica que el controlador devuelva un estado HTTP 404 y un mensaje de error.
     */
    @Test
    void updateProduct_notFound() {
        // Configura el mock de BindingResult para indicar que no hay errores de validación.
        when(bindingResult.hasErrors()).thenReturn(false);
        // Configura el mock de productoService para lanzar una excepción de tiempo de ejecución
        // cuando se intenta actualizar un producto (simulando que no se encuentra).
        when(productoService.updateProduct(anyLong(), any(ProductoRequest.class)))
                .thenThrow(new RuntimeException("Producto no encontrado"));

        // Llama al método del controlador con un ID inexistente.
        ResponseEntity<?> response = productoController.updateProduct(99L, new ProductoRequest(), bindingResult);

        // Verifica que el código de estado HTTP sea 404 (Not Found).
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Verifica que el cuerpo de la respuesta sea un mapa.
        assertTrue(response.getBody() instanceof Map);
        // Convierte el cuerpo a un mapa y verifica su contenido.
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertTrue(body.containsKey("error")); // Verifica que contenga la clave 'error'.
        assertEquals("Producto no encontrado", body.get("error")); // Verifica el mensaje de error.
        // Verifica que el método updateProduct del servicio haya sido llamado exactamente una vez.
        verify(productoService, times(1)).updateProduct(anyLong(), any(ProductoRequest.class));
    }

    /**
     * Prueba para la eliminación exitosa de un producto.
     * Verifica que el controlador devuelva un estado HTTP 204.
     */
    @Test
    void eliminar() {
        // Define un ID de prueba.
        Long id = 1L;
        // Configura el mock de productoService para que no haga nada cuando se llame a eliminar.
        doNothing().when(productoService).eliminar(id);

        // Llama al método del controlador.
        ResponseEntity<Void> response = productoController.eliminar(id);

        // Verifica que el código de estado HTTP sea 204 (No Content).
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        // Verifica que el método eliminar del servicio haya sido llamado exactamente una vez con el ID correcto.
        verify(productoService, times(1)).eliminar(id);
    }

    /**
     * Prueba para la obtención de todos los tipos de productos (categorías).
     * Verifica que el controlador devuelva un estado HTTP 200 y la lista de categorías.
     */
    @Test
    void getTypes() {
        // Prepara una lista simulada de categorías.
        List<CategoriaResponse> categorias = List.of(new CategoriaResponse(), new CategoriaResponse());
        // Configura el mock de categoriaService para devolver la lista simulada.
        when(categoriaService.listarCategorias()).thenReturn(categorias);

        // Llama al método del controlador.
        ResponseEntity<List<CategoriaResponse>> response = productoController.getTypes();

        // Verifica que el código de estado HTTP sea 200 (OK).
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Verifica que el tamaño de la lista en el cuerpo de la respuesta sea el esperado.
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        // Verifica que el método listarCategorias del servicio haya sido llamado exactamente una vez.
        verify(categoriaService, times(1)).listarCategorias();
    }
}