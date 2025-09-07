package com.example.productos.service;

import com.example.productos.domain.Producto;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
@TestMethodOrder(OrderAnnotation.class)
class ProductoServiceTest {

	@Autowired
	private ProductoService service;

	@Test
	@Order(1)
	void crearYObtenerProducto() {
		Producto creado = service.crear("Monitor", new BigDecimal("599.90"), 3);
		Producto obtenido = service.obtenerPorId(creado.getId());
		assertThat(obtenido.getNombre()).isEqualTo("Monitor");
	}

	/**
	 * @Usuario Sergio
	 * @Descripcion Edita los datos de un producto, assertThatThrownBy para validar
	 *              que produzca el error esperado
	 */
	@Test
	@Order(2)
	void editarYObtenerProducto() {
		assertThatThrownBy(() -> service.editar("Monitor Curvo", new BigDecimal("895.13"), 45))
				.isInstanceOf(NotFoundException.class);
		assertThatThrownBy(() -> service.obtenerPorNombre("Monitor Curvo")).isInstanceOf(NotFoundException.class);
	}

	@Test
	@Order(4)
	void eliminarProductoNoExistenteLanzaExcepcion() {
		assertThatThrownBy(() -> service.eliminar(999L)).isInstanceOf(NotFoundException.class);
	}

	@Test
	@Order(3)
	void precioNegativoLanzaIllegalArgument() {
		assertThatThrownBy(() -> service.crear("Tablet", new BigDecimal("-1.00"), 1))
				.isInstanceOf(IllegalArgumentException.class);
	}

	/**
	 * @Usuario Sergio
	 * @Descripcion Lista todos los productos
	 */
	@Test
	@Order(5)
	void listarProductos() {
		List<Producto> lista = new ArrayList<>();
		lista = service.listar();
		assertFalse(lista.isEmpty(), "Lista de Productos Vacía");
	}

	/**
	 * @Usuario Sergio
	 * @Descripcion Lista el producto por nombre
	 */
	@Test
	@Order(6)
	void listaProductoByNombre() {
		assertThatThrownBy(() -> service.obtenerPorNombre("Monitor Curvo")).isInstanceOf(NotFoundException.class);
	}

	/**
	 * @Usuario Sergio
	 * @Descripcion Valida un producto no existente al editar
	 */
	@Test
	@Order(7)
	void editarProductoNoExistente() {
		assertThatThrownBy(() -> service.editar("iphone 15 Pro Max", new BigDecimal("1500.00"), 3))
				.isInstanceOf(NotFoundException.class);
	}
}
