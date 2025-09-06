package com.example.productos.controller;

import com.example.productos.domain.Producto;
import com.example.productos.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductoRepository productoRepository;

    @BeforeEach
    void setUp() {
        productoRepository.deleteAll();
        productoRepository.save(new Producto("Producto 1", BigDecimal.valueOf(10.0), 5));
        productoRepository.save(new Producto("Producto 2", BigDecimal.valueOf(20.0), 10));
    }

    @Test
    void testListarProductos() throws Exception {
        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testCrearProducto() throws Exception {
        String nuevoProductoJson = """
            {
              "nombre": "Producto 3",
              "precio": 30.0,
              "stock": 15
            }
            """;

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nuevoProductoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Producto 3"));
    }

    @Test
    void testBuscarProductoPorId() throws Exception {
        Producto producto = productoRepository.save(
                new Producto("ProductoX", BigDecimal.valueOf(99.99), 3)
        );

        mockMvc.perform(get("/productos/{id}", producto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ProductoX"));
    }

    @Test
    void testBuscarProductoNoExistente() throws Exception {
        mockMvc.perform(get("/productos/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarProducto() throws Exception {
        Producto producto = productoRepository.save(
                new Producto("ProductoEliminar", BigDecimal.valueOf(50.0), 7)
        );

        mockMvc.perform(delete("/productos/{id}", producto.getId()))
                .andExpect(status().isNoContent());
    }
}
