package com.example.productos.repository;

import com.example.productos.domain.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository repository;

    @Test
    void guardarYBuscarPorId() {
        Producto p = new Producto("Teclado", new BigDecimal("99.99"), 10);
        Producto saved = repository.save(p);
        Optional<Producto> found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Teclado");
    }

    @Test
    void eliminarProducto() {
        Producto p = new Producto("Mouse", new BigDecimal("49.99"), 5);
        Producto saved = repository.save(p);
        repository.deleteById(saved.getId());
        assertThat(repository.findById(saved.getId())).isEmpty();
    }
}
