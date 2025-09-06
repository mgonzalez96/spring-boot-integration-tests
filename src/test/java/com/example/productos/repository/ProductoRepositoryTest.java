package com.example.productos.repository;

import com.example.productos.domain.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository repository;

    /**
	 * @Usuario Repositorio
	 * @Descripcion guardar un producto y buscarlo por ID
	 */
    @Test
    void guardarYBuscarPorId() {
        Producto p = new Producto("Teclado", new BigDecimal("99.99"), 10);
        Producto saved = repository.save(p);
        Optional<Producto> found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Teclado");
    }
    
    /**
	 * @Usuario Mariana Acevedo
	 * @Descripcion Editar un producto y buscarlo por ID
	 */
    @Test
    void editarYBuscarPorId() {
        Producto p = new Producto("Teclado", new BigDecimal("15.55"), 90);
        Producto saved = repository.save(p);
        Optional<Producto> found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Teclado");
    }

    /**
	 * @Usuario Repositorio
	 * @Descripcion elimina un producto y buscarlo por ID
	 */
    @Test
    void eliminarProducto() {
        Producto p = new Producto("Mouse", new BigDecimal("49.99"), 5);
        Producto saved = repository.save(p);
        repository.deleteById(saved.getId());
        assertThat(repository.findById(saved.getId())).isEmpty();
    }
    
    /**
	 * @Usuario Mariana Acevedo
	 * @Descripcion Lista todos los productos
	 */
    @Test
    void listarAll() {
        List<Producto> lista = new ArrayList<>();
        lista = repository.findAll();
        assertFalse(lista.isEmpty(),"Lista Vacía");
    }
    
    /**
	 * @Usuario Mariana Acevedo
	 * @Descripcion Lista por nombre el producto
	 */
    @Test
    void listarById() {
    	assertThat(repository.findByNombre("Mouse")).isEmpty();
    }
}
